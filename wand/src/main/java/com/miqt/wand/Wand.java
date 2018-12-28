package com.miqt.wand;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.io.File;

/**
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:34:00
 */
public class Wand {
    private static final String CACHE_DEX_PATH = "dex_path";

    private static final int FINISH = 0x1;
    private static final int ERROR = 0x2;
    private static final int NEW_PACK_ATTACH = 0x3;

    private Context mContext;
    private MotorListener mListener;
    private static volatile Wand instance;
    private ClassLoader mClassLoader;
    private Handler mMainHandler;
    private Encrypter mEncrypter;

    private Wand() {
        mMainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FINISH:
                        if (mListener != null) {
                            mListener.initFnish();
                        }
                        break;
                    case ERROR:
                        if (mListener != null) {
                            mListener.initError((Throwable) msg.obj);
                        }
                        break;
                    case NEW_PACK_ATTACH:
                        if (mListener != null) {
                            mListener.onNewPackAttach((File) msg.obj);
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

    public Wand init(Context context) {
        mContext = context;
        mClassLoader = new MyDexClassLoader(
                getCachePath(), mContext.getFilesDir().getAbsolutePath()
                , null, mContext.getClassLoader());
        Message.obtain(mMainHandler, FINISH).sendToTarget();
        return this;
    }

    public Wand encrypter(Encrypter encrypter) {
        instance.mEncrypter = encrypter;
        return get();
    }

    public Wand listener(MotorListener listener) {
        instance.mListener = listener;
        return get();
    }

    public void attachPackUrl(final String url) {
        new Thread() {
            @Override
            public void run() {
                File file = FileUtils.downloadFile(url, getCachePath(), null);
                attachPack(file);
            }
        }.start();
    }

    public void attachPackAsset(String asset) {
        FileUtils.copyFileFromAssets(null, mContext, asset, getCachePath());
        attachPack(new File(getCachePath()));
    }

    public void attachPack(File pack) {
        if (pack == null) {
            return;
        }
        String dataDir = mContext.getCacheDir().getAbsolutePath();
        File file = null;
        if (!pack.getAbsolutePath().contains(dataDir) || mEncrypter != null) {
            file = new File(getCachePath());
            FileUtils.copyFile(mEncrypter, mContext, pack.getAbsolutePath(), file.getAbsolutePath());
        } else {
            file = pack;
        }
        mClassLoader = new MyDexClassLoader(
                file.getAbsolutePath(), mContext.getFilesDir().getAbsolutePath()
                , null, mContext.getClassLoader());
        SPUtils.put(mContext, CACHE_DEX_PATH, file.getAbsolutePath());
        Message.obtain(mMainHandler, NEW_PACK_ATTACH, file).sendToTarget();
    }

    @NonNull
    private String getCachePath() {
        String dex_path = SPUtils.get(mContext, CACHE_DEX_PATH);
        if (!TextUtils.isEmpty(dex_path)) {
            return dex_path;
        }
        String dexDir = mContext.getCacheDir().getAbsolutePath() + "/wand/";
        File dir = new File(dexDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(dir, "wand.dex");
        SPUtils.put(mContext, CACHE_DEX_PATH, file.getAbsolutePath());
        return file.getAbsolutePath();
    }

    public Class<?> loadClass(String classname, ParentalEntrustmentLevel level) throws ClassNotFoundException {
        MyDexClassLoader loader = (MyDexClassLoader) mClassLoader;
        loader.setLevel(level);
        return mClassLoader.loadClass(classname);
    }

    public Context getContext() {
        return mContext;
    }


    public interface MotorListener {
        void initFnish();

        void initError(Throwable throwable);

        void onNewPackAttach(File file);
    }
}
