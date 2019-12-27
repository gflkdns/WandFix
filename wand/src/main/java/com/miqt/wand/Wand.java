package com.miqt.wand;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.miqt.wand.anno.ParentalEntrustmentLevel;
import com.miqt.wand.bean.DexPatch;
import com.miqt.wand.utils.FileUtils;
import com.miqt.wand.utils.SPUtils;
import com.miqt.wand.utils.ThreadPool;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:34:00
 */
public class Wand {
    private static final String LAST_CACHE_DEX_PATH = "last_dex_path";
    private static final String LAST_CACHE_ODEX_PATH = "last_odex_path";

    private String def_dex_path;
    private String def_odex_path;

    private static final int FINISH = 0x1;
    private static final int ERROR = 0x2;
    private static final int NEW_PACK_ATTACH = 0x3;

    private Context mContext;
    private MotorListener mListener;
    private ClassLoader mClassLoader;
    private Handler mMainHandler;

    private Wand(Context context) {
        mContext = context.getApplicationContext();
        def_dex_path = mContext.getFilesDir().getAbsolutePath() + "/wandfix/wandfix.dex";
        def_odex_path = mContext.getFilesDir().getAbsolutePath() + "/wandfix_cache/";
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

    public Wand attachLastPatch() {
        String path = SPUtils.get(mContext, LAST_CACHE_DEX_PATH);
        String odexPath = SPUtils.get(mContext, LAST_CACHE_ODEX_PATH);
        if (!TextUtils.isEmpty(path) && !TextUtils.isEmpty(odexPath)) {
            try {
                attachPack(new DexPatch(path, odexPath));
            } catch (FileNotFoundException e) {
                Log.e("wandfix", "last attach dex not found !");
            }
        }
        return this;
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

    public Wand init(boolean isAttachLastPatch) {
        if (isAttachLastPatch) {
            attachLastPatch();
        }
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
                File file = FileUtils.downloadFile(url, def_dex_path, null);
                if (file != null) {
                    try {
                        attachPack(file);
                    } catch (Throwable e) {
                    }
                }
            }
        });
    }

    public void attachPackAsset(String asset) {
        FileUtils.copyFileFromAssets(mContext, asset, def_dex_path);
        try {
            attachPack(new File(def_dex_path));
        } catch (Throwable e) {
        }
    }

    public DexPatch attachPack(File pack) throws FileNotFoundException {
        DexPatch patch = new DexPatch(pack.getAbsolutePath(), def_odex_path);
        attachPack(patch);
        return patch;
    }

    public void attachPack(DexPatch dexPatch) {
        try {
            if (dexPatch == null) {
                return;
            }


            ClassLoader parent = null;
            if (mClassLoader != null) {
                parent = mClassLoader.getParent();
            } else {
                parent = mContext.getClassLoader().getParent();
            }
            mClassLoader = new WandClassLoader(dexPatch.getDexFilePath(), dexPatch.getCacheFilePath(), null, parent, new WandClassLoader.Callback() {
                @Override
                public void onLoadClass(ClassLoader loader, String name) {
                    Log.d("wandfix_loadclass", name + " --> " + loader.getClass().getName());
                }

                @Override
                public void onNotFound(String name) {
                    Log.e("wandfix_loadclass", name + " --> " + "not found");
                }
            });
            HackClassLoader.hackParentClassLoader(mContext.getClassLoader(), mClassLoader);
            Message.obtain(mMainHandler, NEW_PACK_ATTACH, dexPatch.getDexFilePath()).sendToTarget();

            SPUtils.put(mContext, LAST_CACHE_DEX_PATH, dexPatch.getDexFilePath());
            SPUtils.put(mContext, LAST_CACHE_ODEX_PATH, dexPatch.getCacheFilePath());
        } catch (Throwable e) {
            Message.obtain(mMainHandler, ERROR, e).sendToTarget();
        }
    }

    public Class<?> loadClass(String classname, ParentalEntrustmentLevel level) throws ClassNotFoundException {
        if (mClassLoader == null) {
            return mContext.getClassLoader().loadClass(classname);
        }
        return mClassLoader.loadClass(classname);
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
