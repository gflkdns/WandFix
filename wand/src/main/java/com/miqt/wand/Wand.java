package com.miqt.wand;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;


public class Wand {
    private static final int FINISH = 0x1;
    private static final int ERROR = 0x2;
    private Context context;
    private MotorListener listener;
    private static volatile Wand instance;
    private ClassLoader mClassLoader;
    private Handler mMainHandler;
    private File dexFile;

    private Wand() {
        mMainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FINISH:
                        if (listener != null) {
                            listener.initFnish();
                        }
                        break;
                    case ERROR:
                        if (listener != null) {
                            listener.initError((Throwable) msg.obj);
                        }
                        break;
                }
            }
        };
    }

    public static Wand get() {
        if (instance == null) {
            synchronized (Wand.class) {
                if (instance == null) {
                    instance = new Wand();
                }
            }
        }
        return instance;
    }

    public static void init(Context context, String assetDex, MotorListener listener) {
        get();
        instance.context = context;
        instance.listener = listener;
        //加载dex
        instance.initClassLoader(assetDex);
        //todo 网络检查dex更新
    }

    public static void init(Context context) {
        init(context, "wand.dex", null);
    }

    public boolean attachDex(File dex) {
        String dataDir = context.getCacheDir().getAbsolutePath();
        File file = null;
        if (!(dex != null && dex.getAbsolutePath().contains(dataDir))) {
            String dexDir = context.getCacheDir().getAbsolutePath() + "/wand/";
            File dir = new File(dexDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            file = new File(dir, "mydex.dex");
            FileUtils.copyFile(context, dex.getAbsolutePath(), file.getAbsolutePath());
        }
        ClassLoader lastLoader = mClassLoader;
        mClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(), context.getFilesDir().getAbsolutePath()
                , null, context.getClassLoader());
        if (mClassLoader != null) {
            dexFile = file;
            return true;
        } else {
            mClassLoader = lastLoader;
            return false;
        }
    }

    private void initClassLoader(String asset) {
        String dexDir = context.getCacheDir().getAbsolutePath() + "/dex/";
        File dir = new File(dexDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        dexFile = new File(dir, "wand.dex");
        if (!(dexFile.exists() && dexFile.isFile() && dexFile.length() > 0)) {
            FileUtils.copyFileFromAssets(context, asset, dexFile.getAbsolutePath());
        }
        mClassLoader = new DexClassLoader(
                dexFile.getAbsolutePath(), context.getFilesDir().getAbsolutePath()
                , null, context.getClassLoader());
        if (mClassLoader == null) {
            Message.obtain(mMainHandler, ERROR, new IllegalStateException("dex file damage."));
        } else {
            Message.obtain(mMainHandler, FINISH);
        }
    }


    public ClassLoader getClassLoader() {
        return mClassLoader;
    }

    public Context getContext() {
        return context;
    }


    public interface MotorListener {
        void initFnish();

        void initError(Throwable throwable);
    }
}
