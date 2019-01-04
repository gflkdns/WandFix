# WandFix

## About

当你的项目已经上线，一个BUG被发现却已经为时已晚，这时候悔恨自责都已经于事无补，甚至还会被伙伴们打上不靠谱的标签，这时候你想不想像哈利波特一样，魔法杖一挥BUG瞬间消除，没错拿着WindFix你就可以实现这一点。

WandFix是一个基于Java ClassLoader实现的热修复框架。

使用WandFix可以不再用 new 来创建对象，只需要对变量名添加注解即可实现对象的注入。而热修复功能的实现既是用对象注入的这种途径，通过更换热修复包替换实现类，来达到每次注入的对象都为最后改动过的，实现热修复的目的。当然，如果您不想使用热修复功能，只是想方便的注入对象，直接添加注解也是可以的，因为如果它在热修复包中没有找到对应的实现类的话，会尝试从项目本地来加载对应的类，保证程序的稳定。

本项目Demo效果预览：

![demo预览](./preview/demo.gif)


优点：
- 类似于黄油刀可以直接对成员变量添加@InjectObject("实现类全名")注解,来绑定热修复包中的实现类。
- 无需关闭应用即可使修复包生效。
- 与mvp模式搭配使用效果最佳。
- 可以自己定义需要热修复的类。
- 可以自己配置dex加密算法，保护dex文件的安全。
- 可以通过注解单独设置某个对象是否禁用双亲委托。

## 使用方法：

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
## Other

[如何创建并应用热修复包](https://github.com/miqt/WandFix/wiki/%E5%A6%82%E4%BD%95%E5%88%9B%E5%BB%BA%E5%B9%B6%E5%BA%94%E7%94%A8%E7%83%AD%E4%BF%AE%E5%A4%8D%E5%8C%85)

具体更多用法请移步[Wiki](https://github.com/miqt/WandFix/wiki)

> 欢迎提出问题和宝贵意见。如果您觉得这个项目还不错，就点个star吧(￣▽￣)~*

