
## 本项目是一个基于java ClassLoader实现的热修复框架。

![demo预览](./preview/demo.gif)

优点：
- 类似于黄油刀可以直接对成员变量添加@InjectObject("com.example.motordex.AppParsenterImpl2")注解,来绑定热修复包中的实现类。
- 无需关闭应用即可使修复包生效。
- 与mvp模式搭配使用效果最佳。
- 可以自己定义需要热修复的类。
- 可以自己配置dex加密算法，保护dex文件的安全。
- 可以通过注解单独设置某个对象是否禁用双亲委托。


使用方法：
```
git clone https://github.com/miqt/WandFix.git
```

添加依赖：

```
compile project(':wand')
annotationProcessor project(':wand-compiler')
```

代码调用：


```java
public class MainActivity extends AppCompatActivity {

    @InjectObject(
        "com.example.motordex.AppParsenterImpl2"//热修复包中的实现类
            )
    AppParsenter ap;

    @InjectObject(
            value = "com.example.motordex.AppParsenterImpl2",//热修复包中的实现类
            level = ParentalEntrustmentLevel.PROJECT//启用双亲委托，优先加载本地类
            )
    AppParsenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ...
        //初始化，可以放在application中调用一次即可
        Wand.init(this);
        //单个参数
        ClassInstall.inject(this);

        //多个参数的构造方法
        //Map<String, Object[]> map = new HashMap<>();
        //map.put("com.example.motordex.AppParsenterImpl2", new Object[]{1, "参数2", "参数3"});
        //ClassInstall.inject(this, map);

        //调用
        String str = ap.getStr();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
        ...
    }
}
```

[如何创建并应用热修复包](https://github.com/miqt/WandFix/wiki/%E5%A6%82%E4%BD%95%E5%88%9B%E5%BB%BA%E5%B9%B6%E5%BA%94%E7%94%A8%E7%83%AD%E4%BF%AE%E5%A4%8D%E5%8C%85)

具体更多用法请移步[Wiki](https://github.com/miqt/WandFix/wiki)

> 欢迎提出问题和宝贵意见。如果您觉得这个项目还不错，就点个star吧(￣▽￣)~*

