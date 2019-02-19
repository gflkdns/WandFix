package com.miqt.wand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.miqt.wand.ObjectFactory;
import com.miqt.wand.ProxyActivityLauncher;

/**
 * Created by t54 on 2019/2/19.
 * 被代理器完全支配的activity
 * @see ActivityProxy
 */

public class HostActy extends AppCompatActivity {
    ActivityProxy proxy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String proxyName = getIntent().getStringExtra(ProxyActivityLauncher.CLASSNAME);
        proxy = ObjectFactory.make(proxyName,this);
        proxy.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        proxy.onStart();
    }


    @Override
    protected void onResume() {
        super.onResume();
        proxy.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        proxy.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        proxy.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        proxy.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        proxy.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        proxy.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        proxy.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
