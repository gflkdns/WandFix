package com.miqt.wand_compiler;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.ArrayList;

import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

public class AnnotatedClass {

    private TypeElement mTypeElement;
    private ArrayList<InjectObjectField> mObjectFields;
    private Elements mElements;

    public AnnotatedClass(TypeElement typeElement, Elements elements) {
        mTypeElement = typeElement;
        mElements = elements;
        mObjectFields = new ArrayList<>();
    }

    public String getFullClassName() {
        return mTypeElement.getQualifiedName().toString();
    }

    public void addField(InjectObjectField field) {
        mObjectFields.add(field);
    }


    public JavaFile generateFile() {
        //generateMethod
        MethodSpec.Builder injectMethod = MethodSpec.methodBuilder("inject")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(TypeName.get(mTypeElement.asType()), "host", Modifier.FINAL)
                .addParameter(TypeName.OBJECT, "source")
                .addParameter(TypeUtil.PROVIDER, "provider");

        for (InjectObjectField field : mObjectFields) {
            // find views
            injectMethod.addStatement("host.$N = ($T)(provider.make(source, \"$L\"))",
                    field.getFieldName(),
                    ClassName.get(field.getFieldType()),
                    field.getResId());
        }

        //generaClass
        TypeSpec injectClass = TypeSpec.classBuilder(mTypeElement.getSimpleName() + "$$ObjectInject")
                .addModifiers(Modifier.PUBLIC)
                .addSuperinterface(ParameterizedTypeName.get(TypeUtil.INJET, TypeName.get(mTypeElement.asType())))
                .addMethod(injectMethod.build())
                .build();

        String packgeName = mElements.getPackageOf(mTypeElement).getQualifiedName().toString();

        return JavaFile.builder(packgeName, injectClass).build();
    }
}
