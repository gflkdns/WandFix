package com.miqt.demo;

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
}
