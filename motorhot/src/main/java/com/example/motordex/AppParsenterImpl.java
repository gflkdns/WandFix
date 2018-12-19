package com.example.motordex;

import com.example.motorlib.AppParsenter;

public class AppParsenterImpl implements AppParsenter {

    String string;

    public AppParsenterImpl(Integer a, String b, String string) {
        this.string = "321313";
    }

    @Override
    public String getStr() {
        return string;
    }
}
