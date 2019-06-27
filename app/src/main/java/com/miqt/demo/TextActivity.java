package com.miqt.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.miqt.demo.proxy.TextActivityProxy;
import com.miqt.demo.teststatic.GetString;
import com.miqt.wand.activity.ProxyActivity;
import com.miqt.wand.anno.BindProxy;

//绑定代理类
@BindProxy(clazz = TextActivityProxy.class)
//必须继承 ProxyActivity
public class TextActivity extends ProxyActivity {
    //这里什么都不用写


    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, GetString.getString() + GetString.bbb + GetString.ccc + new GetString().ddd, Toast.LENGTH_SHORT).show();
    }
}
