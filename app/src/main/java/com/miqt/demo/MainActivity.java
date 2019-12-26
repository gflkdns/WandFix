package com.miqt.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.miqt.demo.R;
import com.miqt.demo.presenter.AppPresenter;
import com.miqt.wand.ClassInstall;
import com.miqt.wand.Wand;
import com.miqt.wand.activity.ProxyActivity;
import com.miqt.wand.anno.InjectObject;
import com.miqt.wand.anno.ParentalEntrustmentLevel;


public class MainActivity extends ProxyActivity implements Wand.MotorListener {
    ProgressDialog mDialog;

    @InjectObject(
            //指向类的全名
            className = "com.miqt.demo.presenter.AppPresenterImpl",
            //设置双亲委托
            //项目开发中建议使用PROJECT，优先应用本地类库。
            //项目发布时，应修改为NEVER，优先应用热修复包中的类库。
            level = ParentalEntrustmentLevel.NEVER)
    AppPresenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ClassInstall.inject(this);
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
        Toast.makeText(this, ap.getStr(), Toast.LENGTH_SHORT).show();
    }
}
