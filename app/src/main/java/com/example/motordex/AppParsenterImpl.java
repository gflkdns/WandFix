package com.example.motordex;

import com.example.motorlib.AppParsenter;

public class AppParsenterImpl implements AppParsenter {

    String string;

    public AppParsenterImpl() {
        this.string = "aaaa";
    }

    @Override
    public String getStr() {
        return string;
    }
}
