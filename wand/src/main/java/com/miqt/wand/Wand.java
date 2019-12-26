package com.miqt.wand;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;

import com.miqt.wand.anno.ParentalEntrustmentLevel;
import com.miqt.wand.bean.DexPatch;
import com.miqt.wand.utils.FileUtils;
import com.miqt.wand.utils.SPUtils;
import com.miqt.wand.utils.ThreadPool;

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
    private ClassLoader mClassLoader;
    private Handler mMainHandler;

    private Wand(Context context) {
        mContext = context.getApplicationContext();
        mMainHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FINISH:
                        if (mListener != null) {
                            mListener.initFinish();
                        }
                        break;
                    case ERROR:
                        if (mListener != null) {
                            mListener.initError((Throwable) msg.obj);
                        }
                        break;
                    case NEW_PACK_ATTACH:
                        if (mListener != null) {
                            mListener.onNewPackAttach((String) msg.obj);
                        }
                        break;
                    default: {
                    }
                }
            }
        };
    }

    private static volatile Wand instance = null;

    public static Wand getInstance(Context context) {
        if (instance == null) {
            synchronized (Wand.class) {
                if (instance == null) {
                    instance = new Wand(context);
                }
            }
        }
        return instance;
    }

    public Wand listener(MotorListener mListener) {
        this.mListener = mListener;
        return this;
    }

    public static Wand get() {
        if (instance == null) {
            throw new IllegalStateException("wand fix 没有初始化");
        }
        return instance;
    }


    public void attachPackUrl(final String url) {
        ThreadPool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                File file = FileUtils.downloadFile(url, getCachePath(), null);
                if (file != null) {
                    attachPack(file);
                }
            }
        });
    }

    public void attachPackAsset(String asset) {
        FileUtils.copyFileFromAssets(mContext, asset, getCachePath());
        attachPack(new File(getCachePath()));
    }

    public DexPatch attachPack(File pack) {
        DexPatch patch = new DexPatch(pack.getAbsolutePath(), mContext.getCacheDir().getAbsolutePath());
        attachPack(patch);
        return patch;
    }

    public void attachPack(DexPatch dexPatch) {
        try {
            mClassLoader = new WandClassLoader(dexPatch.getDexFilePath(), dexPatch.getCacheFilePath(), null, mContext.getClassLoader().getParent());
            HackClassLoader.hackParentClassLoader(mContext.getClassLoader(), mClassLoader);
            Message.obtain(mMainHandler, NEW_PACK_ATTACH, dexPatch.getDexFilePath()).sendToTarget();
        } catch (Exception e) {
            e.printStackTrace();
            Message.obtain(mMainHandler, ERROR, e).sendToTarget();
        }
    }


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
        Class result = mClassLoader.loadClass(classname);
        if (result != null) {
            return result;
        }
        if (level == ParentalEntrustmentLevel.NEVER) {
            return null;
        }
        return mContext.getClassLoader().loadClass(classname);
    }

    public Context getContext() {
        return mContext;
    }


    public interface MotorListener {
        void initFinish();

        void initError(Throwable throwable);

        void onNewPackAttach(String file);
    }
}
