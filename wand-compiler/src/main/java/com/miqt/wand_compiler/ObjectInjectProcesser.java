package com.miqt.wand_compiler;

import com.google.auto.service.AutoService;
import com.miqt.wand.anno.HotFixActy;
import com.miqt.wand.anno.InjectObject;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;


@AutoService(Processor.class)
public class ObjectInjectProcesser extends AbstractProcessor {
    private Filer mFiler; //文件相关的辅助类
    private Elements mElementUtils; //元素相关的辅助类
    private Messager mMessager; //日志相关的辅助类
    private Set<String> fieldSet;

    private Map<String, AnnotatedClass> mAnnotatedClassMap;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
        mAnnotatedClassMap = new TreeMap<>();
        fieldSet = new HashSet<>();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        mAnnotatedClassMap.clear();


        try {
            processInjectClass(roundEnv);
            processHotFixActy(roundEnv);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            error(e.getMessage());
        }
        //生成注解对应的类
        for (AnnotatedClass annotatedClass : mAnnotatedClassMap.values()) {
            try {
                annotatedClass.generateFile().writeTo(mFiler);
            } catch (IOException e) {
                error("Generate file failed, reason: %s", e.getMessage());
            }
        }
        makePack();
        return true;
    }

    private void processHotFixActy(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(HotFixActy.class)) {
            TypeElement element1= (TypeElement) element;
            fieldSet.add(element1.getQualifiedName().toString());
        }
    }

    private void makePack() {
        if (fieldSet.isEmpty()) {
            return;
        }
        StringBuilder builder = new StringBuilder();
        builder.append("cd ./app/build/intermediates/classes/debug/").append("\n")
                .append("jar cvf hotfix_pack.jar ");
        for (String className : fieldSet) {
            builder.append(" ./")
                    .append(className.replace('.', '/'))
                    .append(".class");
        }
        builder.append("\n");
        builder.append("dx --dex --output=../../../../../hotfix_pack.dex hotfix_pack.jar\n");
        try {
            FileOutputStream outputStream = new FileOutputStream("./make_fix_pack.bat");
            outputStream.write(builder.toString().getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void processInjectClass(RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(InjectObject.class)) {
            AnnotatedClass annotatedClass = getAnnotatedClass(element);
            InjectObjectField fieid = new InjectObjectField(element);
            annotatedClass.addField(fieid);
            fieldSet.add(fieid.getClassName());
        }
    }


    private AnnotatedClass getAnnotatedClass(Element element) {
        TypeElement typeElement = (TypeElement) element.getEnclosingElement();
        String fullName = typeElement.getQualifiedName().toString();
        AnnotatedClass annotatedClass = mAnnotatedClassMap.get(fullName);
        if (annotatedClass == null) {
            annotatedClass = new AnnotatedClass(typeElement, mElementUtils);
            mAnnotatedClassMap.put(fullName, annotatedClass);
        }
        return annotatedClass;
    }

    //指定java版本SourceVersion.latestSupported();
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    /**
     * 指定哪些注解应该被注解处理器注册
     *
     * @return
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> types = new LinkedHashSet<>();
        types.add(InjectObject.class.getCanonicalName());
        types.add(HotFixActy.class.getCanonicalName());
        return types;
    }

    private void error(String msg, Object... args) {
        mMessager.printMessage(Diagnostic.Kind.ERROR, String.format(msg, args));
    }
}
