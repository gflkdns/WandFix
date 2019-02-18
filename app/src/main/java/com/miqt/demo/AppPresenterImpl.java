package com.miqt.demo;

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
        return name.toLowerCase();
    }
}
