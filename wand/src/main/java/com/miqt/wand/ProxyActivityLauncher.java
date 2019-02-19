package com.miqt.wand;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.miqt.wand.activity.ActivityProxy;
import com.miqt.wand.activity.HostActy;

/**
 * Created by t54 on 2019/2/19.
 */

public class ProxyActivityLauncher {
    public static final String CLASSNAME = "class_name_wand";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void startActivityForResult(Activity acty, Class<? extends ActivityProxy> clazz,
                                              @Nullable Intent intent, int startFlag,
                                              int requestCode, @Nullable Bundle options) {
        if (intent == null) {
            intent = new Intent();
        }
        intent.setClass(acty, HostActy.class);
        if (startFlag != -1) {
            intent.addFlags(startFlag);
        }
        intent.putExtra(CLASSNAME, clazz.getName());
        acty.startActivityForResult(intent, requestCode, options);
    }

    public static void startActivity(Activity acty, Class<? extends ActivityProxy> clazz, Intent intent, int startFlag) {
        startActivityForResult(acty, clazz, intent, startFlag, -1, null);
    }

    public static void startActivity(Activity acty, Class<? extends ActivityProxy> clazz) {
        startActivityForResult(acty, clazz, null, -1, -1, null);
    }
}
