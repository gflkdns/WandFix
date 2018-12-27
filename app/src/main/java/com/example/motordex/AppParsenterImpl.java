package com.example.motordex;

import com.example.motorlib.AppParsenter;

public class AppParsenterImpl implements AppParsenter {

    public AppParsenterImpl() {
    }

    @Override
    public String getStr() {
        return String.valueOf(1 / 0);
    }
}
