package com.miqt.demo.clazz;

import com.miqt.wand.anno.AddToFixPatch;

/**
 * 这个类用来测试wandfix对类的元素的修复情况
 */
@AddToFixPatch
public class TextFixBean implements ITextFixBean {
    public String public_String = "public_String";
    public static String public_static_String = "public_static_String";
    public final static String public_final_static_String = "public_final_static_String";

    public int public_int = 1;
    public static int public_static_int = 1;
    public final static int public_final_static_int = 1;

    @Override
    public void setStaticString(String str) {
        public_static_String = str;
    }

    @Override
    public String getStaticString() {
        return public_static_String;
    }

    @Override
    public int getStaticInt() {
        return public_static_int;
    }

    @Override
    public void setStaticInt(int num) {
        public_static_int = num;
    }

    @Override
    public int getFinalStaticInt() {
        return public_final_static_int;
    }

    @Override
    public String getFinalStaticString() {
        return public_final_static_String;
    }

    @Override
    public String getString() {
        return public_String;
    }

    @Override
    public void setString(String str) {
        public_String = str;
    }

    @Override
    public int getInt() {
        return public_int;
    }

    @Override
    public void setInt(int num) {
        public_int = num;
    }

    public static class StaticInnerC {
        public static String getString(String str) {
            return str;
        }
    }

    public class InnerC {
        public String getString(String str) {
            return str;
        }
    }


}
