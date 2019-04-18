package com.miqt.wand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;

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

    /**
     * 为什么要用这个方法<br><br/>主要用于解决主module模块R文件是用final修饰的，
     * 而用final修饰的对象在java中视为常量，在编辑成.class文件的时候，这个常量
     * 不会使用R.id.***的方式去引用而是直接是数字。因此这个值因为我们每次编译造成同一个id实际的id数字不一致，导致
     * findviewid找出来的view为空或者类型异常。
     * <p>
     * <br><br/>
     * <p>
     * 这个方法就是为了解决这个问题
     *
     * @param id 填“R.id.idname”格式一定要正确，最好是先用android的findviewbyid然后再加上双引号就好了
     */
    public <T extends View> T findViewById(@NonNull String id) {
        return (T) mActy.findViewById(analyzeRealId(id));
    }

    private int analyzeRealId(String id) {
        String[] idElements = id.split("\\.");
        if (idElements.length < 3) {
            return -1;
        }
        String idname = idElements[idElements.length - 1];
        String idtype = idElements[idElements.length - 2];
        String R = idElements[idElements.length - 3];
        StringBuilder packagenameBuilder = new StringBuilder();
        if (idElements.length > 3) {
            for (int i = 0; i < idElements.length - 4; i++) {
                packagenameBuilder.append(idElements[i]).append(".");
            }
        }
        String packagename = packagenameBuilder.toString();
        if (TextUtils.isEmpty(packagename)) {
            packagename = mActy.getPackageName();
        }
        return mActy.getResources().getIdentifier(idname, idtype, packagename);
    }

    public void setContentView(String layoutResID) {
        mActy.setContentView(analyzeRealId(layoutResID));
    }

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
