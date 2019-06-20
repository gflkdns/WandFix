package com.miqt.demo.teststatic;

import com.miqt.wand.anno.AddToFixPatch;

@AddToFixPatch
public class GetString {
    public static final String ccc = "ccc1";
    public static String bbb = "bbb1";

    public static String getString() {
        return "aaa1";
    }
}
