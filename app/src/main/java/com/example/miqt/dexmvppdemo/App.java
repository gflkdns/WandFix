package com.example.miqt.dexmvppdemo;

import android.app.Application;

import com.miqt.wand.ClassInstall;
import com.miqt.wand.Wand;

/**
 * Created by t54 on 2019/1/17.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 wandfix
        Wand.get().init(this);
    }
}
