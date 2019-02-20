package com.miqt.wand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.miqt.wand.ObjectFactory;
import com.miqt.wand.anno.BindProxy;
import com.miqt.wand.anno.ParentalEntrustmentLevel;

/**
 * Created by miqt on 2019/2/19.
 * 被代理器完全支配的activity
 *
 * @see ActivityProxy
 */

public class ProxyActivity extends AppCompatActivity {
    ActivityProxy proxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BindProxy proxyInfo = this.getClass().getAnnotation(BindProxy.class);
        //如果这个类没添加这个注解，证明这个activity不需要代理，直接返回
        if (proxyInfo == null) {
            return;
        }
        String proxyName = this.getClass().getAnnotation(BindProxy.class).clazz().getName();
        ParentalEntrustmentLevel level = this.getClass().getAnnotation(BindProxy.class).level();
        if (proxyName != null && proxyName.length() != 0) {
            proxy = ObjectFactory.make(proxyName, level, this);
            if (proxy != null) {
                proxy.onCreate(savedInstanceState);
            }
        }
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
