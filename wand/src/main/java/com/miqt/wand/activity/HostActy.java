package com.miqt.wand.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.miqt.wand.ObjectFactory;

/**
 * Created by miqt on 2019/2/19.
 * 被代理器完全支配的activity
 *
 * @see ActivityProxy
 */

public class HostActy extends AppCompatActivity {
    public static final String CLASSNAME = "class_name_wand";
    ActivityProxy proxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String proxyName = getIntent().getStringExtra(CLASSNAME);
        if (proxyName != null && proxyName.length() != 0) {
            proxy = ObjectFactory.make(proxyName, this);
            if (proxy != null) {
                proxy.onCreate(savedInstanceState);
            }
        }
    }

    /**
     * 通过指定代理的方式，启动一个activity
     * @param intent
     * @param clazz 代理class
     */
    public void startProxyActivity(Intent intent, Class<? extends ActivityProxy> clazz) {
        intent.putExtra(CLASSNAME, clazz.getName());
        startActivity(intent);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void startProxyActivityForResult(Intent intent, Class<? extends ActivityProxy> clazz, int requestCode, Bundle option) {
        intent.putExtra(CLASSNAME, clazz.getName());
        startActivityForResult(intent, requestCode, option);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (proxy != null) {
            proxy.onStart();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (proxy != null) {
            proxy.onResume();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (proxy != null) {
            proxy.onRestart();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (proxy != null) {
            proxy.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (proxy != null) {
            proxy.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (proxy != null) {
            proxy.onDestroy();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (proxy != null) {
            proxy.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (proxy != null) {
            proxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
}
