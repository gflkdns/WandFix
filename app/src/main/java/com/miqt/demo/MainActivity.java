package com.miqt.demo;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.miqt.demo.R;
import com.miqt.demo.proxy.MyActivityProxy;
import com.miqt.wand.ClassInstall;
import com.miqt.wand.ProxyActivityLauncher;
import com.miqt.wand.Wand;
import com.miqt.wand.anno.InjectObject;
import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.io.File;

public class MainActivity extends AppCompatActivity implements Wand.MotorListener {
    ProgressDialog mDialog;

    @InjectObject(
            //指向类的全名
            value = "com.miqt.demo.presenter.AppPresenterImpl",
            //设置双亲委托
            //项目开发中建议使用PROJECT，优先应用本地类库。
            //项目发布时，应修改为NEVER，优先应用热修复包中的类库。
            level = ParentalEntrustmentLevel.NEVER)
    AppPresenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //可以在application中初始化一次即可
        Wand.get().init(this).listener(this);
    }

    public void getStr(View view) {
        //使用注解注入对象必须要调用这一行
        ClassInstall.inject(this);
        //或者带参数的构造方法
        //Map<String, Object[]> pramHouse=new HashMap<>();
        //pramHouse.put("com.miqt.demo.presenter.AppPresenterImpl",new Object[]{"hello"});
        //ClassInstall.inject(this,pramHouse);

        //也可以不使用注解注入的方式初始化对象
        //ap= ObjectFactory.make("com.miqt.demo.presenter.AppPresenterImpl"/*,构造参数*/);
        //ap= ObjectFactory.make(AppPresenterImpl.class/*,构造参数*/);

        String str = ap.getStr();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    public void fix(View view) {
        mDialog = ProgressDialog.show(this, "", "修复中...");
        Wand.get().attachPackUrl("https://github.com/miqt/WandFix/raw/master/hotfix_pack.dex");
    }
    public void proxy(View view) {
        ProxyActivityLauncher.startActivity(this, MyActivityProxy.class);
    }

    @Override
    public void initFinish() {

    }

    @Override
    public void initError(Throwable throwable) {

    }

    @Override
    public void onNewPackAttach(File file) {
        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
        }
        Toast.makeText(this, "一个新的修复包被应用！", Toast.LENGTH_SHORT).show();
    }
}
