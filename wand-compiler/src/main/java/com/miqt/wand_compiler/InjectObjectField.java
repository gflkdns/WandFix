package com.miqt.wand_compiler;


import com.miqt.wand.anno.InjectObject;
import com.miqt.wand.anno.ParentalEntrustmentLevel;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class InjectObjectField {
    private final ParentalEntrustmentLevel mLevel;
    private VariableElement mVariableElement;
    private String mClassName;

    public ParentalEntrustmentLevel getmLevel() {
        return mLevel;
    }

    public InjectObjectField(Element element) throws IllegalArgumentException {
        if (element.getKind() != ElementKind.FIELD) {
            throw new IllegalArgumentException(String.format("Only fields can be annotated with @%s",
                    InjectObject.class.getSimpleName()));
        }
        mVariableElement = (VariableElement) element;

        InjectObject injectOvject = mVariableElement.getAnnotation(InjectObject.class);
        mClassName = injectOvject.value();
        mLevel = injectOvject.level();
        if (mClassName == null) {
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


    public String getClassName() {
        return mClassName;
    }

    /**
     * 获取变量类型
     *
     * @return
     */
    public TypeMirror getFieldType() {
        return mVariableElement.asType();
    }

    @Override
    public int hashCode() {
        return getClassName().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InjectObjectField that = (InjectObjectField) o;

        if (mLevel != that.mLevel) return false;
        return mClassName != null ? mClassName.equals(that.mClassName) : that.mClassName == null;
    }
}
