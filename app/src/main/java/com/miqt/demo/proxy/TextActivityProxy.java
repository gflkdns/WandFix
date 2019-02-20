package com.miqt.demo.proxy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.example.miqt.demo.R;
import com.miqt.wand.activity.ActivityProxy;
import com.miqt.wand.activity.ProxyActivity;
import com.miqt.wand.anno.AddToFixPatch;

/**
 * Created by miqt on 2019/2/19.
 */
//每个代理类都要设置
@AddToFixPatch
public class TextActivityProxy extends ActivityProxy {
    public TextActivityProxy(ProxyActivity acty) {
        super(acty);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mActy.setContentView(R.layout.activity_hello);
        TextView textView = mActy.findViewById(R.id.tv_text);
        textView.setText("我是一号代理，我要变成蓝色");
        textView.setTextColor(Color.BLUE);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
