package com.miqt.demo.presenter;

/**
 * Created by miqt on 2019/2/18.
 */

public class AppPresenterImpl implements AppPresenter {
    String name;

    public AppPresenterImpl(CharSequence name) {
        this.name = (String) name + "CharSequence";
    }

    public AppPresenterImpl(String name, int a) {
        this.name = name + "String";
    }

    public AppPresenterImpl(Comparable name) {
        this.name = (String) name + "Comparable";
    }

    public AppPresenterImpl() {
    }

    @Override
    public String getStr() {
        return "2019年3月22日07:29:50 世界早安！";
    }
}
