package com.miqt.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.miqt.demo.R;
import com.miqt.demo.presenter.AppPresenterImpl;
import com.miqt.wand.Wand;
import com.miqt.wand.activity.ProxyActivity;


public class MainActivity extends ProxyActivity implements Wand.MotorListener {
    ProgressDialog mDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void fix(View view) {
        mDialog = ProgressDialog.show(this, "", "修复中...");
        //  Wand.get().listener(this).attachPackUrl("https://github.com/miqt/WandFix/raw/master/hotfix_pack.dex");
        Wand.get().listener(this).attachPackAsset("hotfix_pack.dex");
    }

    public void proxy(View view) {
        //启动一个受到代理的activity
        startActivity(new Intent(this, TextActivity.class));
    }

    @Override
    public void initFinish() {

    }

    @Override
    public void initError(Throwable throwable) {

    }

    @Override
    public void onNewPackAttach(String file) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(this, "一个新的修复包被应用！", Toast.LENGTH_SHORT).show();
    }


    public void getStr(View view) {
        AppPresenterImpl ap = new AppPresenterImpl();
        Toast.makeText(this, ap.getStr(), Toast.LENGTH_SHORT).show();
    }
}
