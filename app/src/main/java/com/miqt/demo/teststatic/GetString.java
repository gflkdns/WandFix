package com.miqt.demo.teststatic;

import com.miqt.wand.anno.AddToFixPatch;

@AddToFixPatch
public class GetString {
    public static final String ccc = "ccc";
    public static String bbb = "bbb";

    public static String getString() {
        return "aaa";
    }

    public String ddd = "ddd";
}
