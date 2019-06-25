package com.miqt.demo.teststatic;

import com.miqt.wand.anno.AddToFixPatch;

@AddToFixPatch
public class GetString {
    public static final String ccc = "ccc2";
    public static String bbb = "bbb2";

    public static String getString() {
        return "aaa2";
    }

    public String ddd = "ddd2";
}
