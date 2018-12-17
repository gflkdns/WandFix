package com.miqt.wand_compiler;


import com.miqt.wand.anno.InjectObject;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class InjectObjectField {
    private VariableElement mVariableElement;
    private String mresId;

    public InjectObjectField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    InjectObject.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;

        InjectObject bindView = mVariableElement.getAnnotation(InjectObject.class);
        mresId = bindView.value();
        if (mresId == null) {
            throw new IllegalArgumentException(
                    String.format("value() in %s for field %s is not valid !", InjectObject.class.getSimpleName(),
                            mVariableElement.getSimpleName()));
        }
    }

    /**
     * 获取变量名称
     *
     * @return
     */
    public Name getFieldName() {
        return mVariableElement.getSimpleName();
    }

    /**
     * 获取变量id
     *
     * @return
     */
    public String getResId() {
        return mresId;
    }

    /**
     * 获取变量类型
     *
     * @return
     */
    public TypeMirror getFieldType() {
        return mVariableElement.asType();
    }
}
