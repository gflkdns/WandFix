package com.miqt.demo;

import com.miqt.demo.proxy.TextActivityProxy;
import com.miqt.wand.activity.ProxyActivity;
import com.miqt.wand.anno.BindProxy;

//绑定代理类
@BindProxy(clazz = TextActivityProxy.class)
//必须继承 ProxyActivity
public class TextActivity extends ProxyActivity {
    //这里什么都不用写
}
