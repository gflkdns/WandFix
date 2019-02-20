package com.miqt.wand.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by miqt on 2019/2/19.
 * activity 代理器
 */

public abstract class ActivityProxy {

    public HostActy mActy;

    public ActivityProxy(HostActy acty) {
        mActy = acty;
    }

    public abstract void onCreate(@Nullable Bundle savedInstanceState);

    public abstract void onStart();

    public abstract void onResume();

    public abstract void onRestart();

    public abstract void onPause();

    public abstract void onStop();

    public abstract void onDestroy();

    public abstract void onActivityResult(int requestCode, int resultCode, Intent data);

    public abstract void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
}
