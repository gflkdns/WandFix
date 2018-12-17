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

    public static void init(Context context, MotorListener listener) {
        get();
        instance.context = context;
        instance.listener = listener;
        //加载dex
        instance.initClassLoader();
        //todo 网络检查dex更新
    }

    public static void init(Context context) {
        init(context, null);
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

    private boolean copyFileFromAssets(Context context, String assetName, String path) {
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
