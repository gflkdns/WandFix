# MVPHotFix
android基于MVP的热修复方案构思，不使用第三方


## 由来

现在开发android项目大部分都已经由mvc转移到了mvp，关于mvp是什么大致也不必多说了，无非三个层：  
- m：model层，一般封装对数据的操作，增删改查，接口访问等等。
- v：view层，也就是视图层，视图层不主动做什么，只是根据某些事件作出对应的视图展示。
- p：Presenter层，也就是逻辑层。

图解(之前在别的博文看到的，觉得比较好就直接拿来用了):  
![MVP](https://upload-images.jianshu.io/upload_images/2413316-e7fe02362c275ddc.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/700)

<!-- more -->

在mvp模式中，model层和view层不再有直接交互，而把相应的工作交给了“中间人”Presenter层来处理。   
因此在我们的项目中，Presenter层可以说是一个改动比较频繁，逻辑比较复杂的一个模块。也是较容易出现问题的一个模块，对Presenter层实现热修复，对项目的稳定性是十分有利的。   
总结一下：
- 项目背景：mvp的项目。  
- 需求：在不修改model层和view层的基础上，实现对Presenter层的热修复。  

## 构想

核心思想就是Presenter层只写接口，然后使用java的classloader机制加载Presenter层的实现类来产生对象然后赋值给接口指针调用。通过不停的更换classloader所加载的文件，但调用方法一致，来达到热修复的目的。   
下面是我画的一个整体的结构图。  

![mvphotfix结构图](http://imaster.top/blogimgs/mvp_hot_fix.jpg)    

## 尝试实现

既然大体思路都有了，那么咱们就来尝试一下能不能行得通吧。做一个案例，功能非常简单，界面上一个按钮，点击按钮吐司一个字符串，这个字符串通过。   

###  1.创建项目
除了一路next之外我这里想说的实际上是项目module结构：  
![项目结构图](http://imaster.top/blogimgs/20180801154218.png)    

- app里面主要写项目的相关界面。
- motorlib里面主要写presenter接口和热修复、classloadler等相关的代码，另外model层也可以在里面写，其实它们可以写在app里面，但这样组件化的话，更有利于解耦。  
- motorhot这里就是写presenter的具体实现了，另外model层也可以在里面写，那样的话对于后台接口返回的数据格式变更等也可以进行修复了。  

再设置一下这三个module之间的引用关系。

.\app\build.gradle
```
dependencies {
    ...
    //只关心接口，不关心实现，因此只引用motorlib
    implementation project(':motorlib')
}
```
.\motorhot\build.gradle
```
dependencies {
    ...
    //需要集成motorlib定义的接口，因此需要引用
    implementation project(':motorlib')
}
```
.\motorlib\build.gradle
```
dependencies {
  ...
  //因为只定义接口，所以都不需要引用
}
```

###  2.persenter接口

在motorlib中创建一个java接口，AppParsenter，里面只有一个方法：

```java
public interface AppParsenter {
    String getStr();
}
```
###  3.实现接口persenter接口  
在motorlib中创建一个java类，实现AppParsenter：
```java
public class AppParsenterImpl implements AppParsenter {
    @Override
    public String getStr() {
        return "hello word !";
    }
}
```
###  4.热修复文件打包
在android studio中的右侧，打开Gradle一栏，然后点击（也可以直接运行gradlew命令`gradlew motorhot:assembleRelease`）：  

![打包](http://imaster.top/blogimgs/20180801160206.png)  
打包后的jar包文件存放在`.\motorhot\build\intermediates\bundles\release\classes.jar`  

拿到打包好的classes.jar,然后使用android sdk提供的dx.bat将jar包转换为dex：

```
CD ..\android\sdk\build-tools\20.0.0
dx --dex --output=..\hotfix.dex ..\classes.jar
```
这样,用于热修复的.dex文件就打包完成了。

###  5.ObjectFactory

因为dex的加载离不开`DexClassLoader`，因此我在这里先对DexClassLoader进行了一下封装：

```java
public class Motor {
    private Context context;
    private MotorListener listener;
    private static volatile Motor motor;
    private DexClassLoader mClassLoader;

    private Motor() {
    }

    public static Motor get() {
        if (motor == null) {
            synchronized (Motor.class) {
                if (motor == null) {
                    motor = new Motor();
                }
            }
        }
        return motor;
    }

    public static void init(Context context, MotorListener listener) {
        get();
        motor.context = context;
        motor.listener = listener;
        //加载dex
        motor.initClassLoader();
        listener.initFnish();
        //todo 网络检查dex更新
    }

    private void initClassLoader() {
        String dexDir = context.getCacheDir().getAbsolutePath() + "/dex/";
        File dir = new File(dexDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dexFile = new File(dir, "mydex.dex");
        if (!(dexFile.exists() && dexFile.isFile() && dexFile.length() > 0)) {
            copyFileFromAssets(context, "mydex.dex", dexFile.getAbsolutePath());
        }
        mClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(), context.getFilesDir().getAbsolutePath()
                , null, context.getClassLoader());

    }

    public boolean copyFileFromAssets(Context context, String assetName, String path) {
        boolean bRet = false;
        try {
            InputStream is = context.getAssets().open(assetName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[64];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    public DexClassLoader getClassLoader() {
        return mClassLoader;
    }

    public void setmClassLoader(DexClassLoader mClassLoader) {
        this.mClassLoader = mClassLoader;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public interface MotorListener {
        void initFnish();

        void initError(Throwable throwable);
    }
}
```

单例，首先从assets中把.dex文件拷贝到android的沙盒目录（android加载dex的时候有限制，必须是在沙盒目录中才能加载），然后构建好了mClassLoader就完成了。    

方便起见，热修复文件就不从网络下载了，因此直接把打包好的.dex拷贝到项目的assets文件夹中。   

![打包](http://imaster.top/blogimgs/20180801165443.png)  

在motorlib中创建ObjectFactory，用来通过classloader生产对象：

```java
public class ObjectFactory {
    public static Object make(String type) {
        try {
            Class<AppParsenter> ap = (Class<AppParsenter>) Motor.get()
                    .getClassLoader().loadClass("com.example.motordex.AppParsenterImpl");
            AppParsenter o = ap.newInstance();
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
```
到了这里就可以注意到了，反射构建对象的时候`ap.newInstance();`没法传参数，因此对于Parsenter的实现类中，必须有一个无参的构造方法。

###  6.运行

app中新建一个activity，设置一个按钮然后添加点击事件：  
layout/activity_main.xml  
```xml
<Button
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:onClick="getStr"
    android:text="获取字符串" />
```
com.example.miqt.dexmvppdemo.MainActivity  
```java
public class MainActivity extends AppCompatActivity {
    AppParsenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Motor.init(this, new Motor.MotorListener() {
            @Override
            public void initFnish() {
                ap = (AppParsenter) ObjectFactory.make(MainActivity.class.getName());
            }
            @Override
            public void initError(Throwable throwable) {
            }
        });

    }

    public void getStr(View view) {
        String str = ap.getStr();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
```
运行结果：
![打包](http://imaster.top/blogimgs/Screenshot_2018-08-01-17-11-50-936_com.example.mi.png)
修改motorhot中的AppParsenterImpl，模拟修复了一个bug：
```java
public class AppParsenterImpl implements AppParsenter {
    @Override
    public String getStr() {
        return "fix a bug！";
    }
}
```

重复4步骤，打包运行：
![打包](http://imaster.top/blogimgs/Screenshot_2018-08-01-17-37-41-820_com.example.mi.png)



## 总结

事实证明这种构想完全可以实现，并且可以在不使用其他框架，达到热修复的目的，并且不仅仅是presenter层，在其他的层应用这种方式，也是可以的。


但实际上从上面的实践，也可以发现一些问题：

1. dex文件线上下载或拷贝过程中如果损坏，则可能引起程序崩溃。不过我们可以使用对dex文件取hash码然后对比下载后的文件，如果不一致则证明出错，重新下载拷贝。解决这个问题。
2. dex暴露在用户手机沙盒目录中，而android的沙盒目录在root之后是可以直接访问的，因此有可能被人拿到dex反编译，修改逻辑搞破坏。并且因为反射的影响，motorhot中的类文件是不可以进行混淆的，因为混淆之后就会报找不到类的异常。不过关于这个也是有解决办法的，我想到的那就是对热修复文件加密，在loader之前再在内存中解密。这样别人没法知道加密规则，也就没法解密了。
3. 双亲委托机制，在classloader加载外部dex之前会先检查本地是否已经存在同名的类，如果有则优先加载本地已经存在的类，因此在实际使用中我们最好还要禁用双亲委托机制。

综上，本方案应该是可以在项目中实际应用的一个热修复方案。   


> 完整代码：https://github.com/miqt/MVPHotFix
