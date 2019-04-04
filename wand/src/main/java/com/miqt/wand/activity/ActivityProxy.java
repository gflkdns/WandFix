package com.miqt.wand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.KeyEvent;

/**
 * Created by miqt on 2019/2/19.
 * activity 代理器
 */

public abstract class ActivityProxy {

    public ProxyActivity mActy;

    public ActivityProxy(ProxyActivity acty) {
        mActy = acty;
    }

    public abstract void onCreate(@Nullable Bundle savedInstanceState);

    public void onStart() {
    }

    ;

    public void onResume() {
    }

    ;

    public void onRestart() {
    }

    ;

    public void onPause() {
    }

    ;

    public void onStop() {
    }

    ;

    public void onDestroy() {
    }

    ;

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    ;

    public void onBackPressed() {
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    }

    ;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return false;
    }

    public void onNewIntent(Intent intent) {

    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return false;
    }

    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        return false;
    }
}
