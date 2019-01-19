package com.example.motordex;

import com.example.motorlib.AppParsenter;

public class AppParsenterImpl implements AppParsenter {

    String name;

    public AppParsenterImpl(CharSequence name) {
        this.name = (String) name + "CharSequence";
    }

    public AppParsenterImpl(String name, int a) {
        this.name = name + "String";
    }

    public AppParsenterImpl(Comparable name) {
        this.name = (String) name + "Comparable";
    }

    @Override
    public String getStr() {
        return name;
    }
}
