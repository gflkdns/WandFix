package com.example.motorlib;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import dalvik.system.DexClassLoader;


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
