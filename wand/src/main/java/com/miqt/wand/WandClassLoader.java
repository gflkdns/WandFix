package com.miqt.wand;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import dalvik.system.DexClassLoader;
import dalvik.system.InMemoryDexClassLoader;

/**
 * 一个反转了双亲委托的classloader，class查找顺序为 自己（内存->缓存->dex文件）-> parent -> 孩子（默认为 context.getClassLoader）
 */
class WandClassLoader extends DexClassLoader {
    /**
     * 用来回调一个类具体是由谁加载了
     */
    private Callback callback;
    /**
     * 记录自己找不到的类
     */
    private Set<String> notFoundClass;
    /**
     * 内存dex
     */
    private InMemoryDexClassLoader memoryDexClassLoader;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public WandClassLoader(ByteBuffer dexBuffers, ClassLoader parent, Callback callback) {
        this("", null, null, parent, callback);
        memoryDexClassLoader = new InMemoryDexClassLoader(dexBuffers, this);
    }

    public WandClassLoader(ByteBuffer[] dexBuffers, String librarySearchPath, ClassLoader parent, Callback callback) {
        this("", null, null, parent, callback);
        memoryDexClassLoader = new InMemoryDexClassLoader(dexBuffers, librarySearchPath, this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O_MR1)
    public WandClassLoader(ByteBuffer[] dexBuffers, ClassLoader parent, Callback callback) {
        this("", null, null, parent, callback);
        memoryDexClassLoader = new InMemoryDexClassLoader(dexBuffers, this);
    }


    public WandClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent, Callback callback) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
        notFoundClass = new HashSet<>();
        this.callback = callback;
    }

    public WandClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        this(dexPath, optimizedDirectory, librarySearchPath, parent, null);
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
        if (notFoundClass.contains(name)) {
            notFoundClass.remove(name);
            //向下双亲委派,告诉孩子："为父也找不到啊"
            throw new ClassNotFoundException(name);
        }
        Class<?> c = null;
        //-------内存文件中找，内存loader有可能会继续因为双亲委托拜托给我，因此我记下来这个类是给内存loader找的，拜托我的时候，直接告诉他"我也找不到啊"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && memoryDexClassLoader != null) {
            try {
                notFoundClass.add(name);
                c = memoryDexClassLoader.loadClass(name);
                if (c != null) {
                    if (callback != null) {
                        callback.onLoadClass(memoryDexClassLoader, name);
                    }
                    return c;
                }
            } catch (ClassNotFoundException e) {
                //适配 patent 链中，没有this的场景
                notFoundClass.remove(name);
            }
        }
        //-------自己缓存里面找
        c = findLoadedClass(name);
        if (c != null) {
            if (callback != null) {
                callback.onLoadClass(this, name);
            }
            return c;
        }
        //-------自己dex中找
        try {
            c = findClass(name);
            if (c != null) {
                if (callback != null) {
                    callback.onLoadClass(this, name);
                }
                return c;
            }
        } catch (ClassNotFoundException e) {

        }
        //--------从父亲中找
        try {
            if (getParent() != null) {
                c = getParent().loadClass(name);
            }
            if (c != null) {
                if (callback != null) {
                    callback.onLoadClass(getParent(), name);
                }
                return c;
            }
        } catch (ClassNotFoundException e) {
        }
        //--------给孩子找，孩子有可能会继续因为双亲委托拜托给我，因此我记下来这个类是给孩子找的，拜托我的时候，直接告诉他"为父也找不到啊"
        notFoundClass.add(name);
        try {
            if (Wand.get().getContext().getClassLoader() != null) {
                c = Wand.get().getContext().getClassLoader().loadClass(name);
            }
            if (c != null) {
                if (callback != null) {
                    callback.onLoadClass(Wand.get().getContext().getClassLoader(), name);
                }
                return c;
            }
        } catch (ClassNotFoundException e) {
            //适配 patent 链中，没有this的场景
            notFoundClass.remove(name);
        }
        //not found error
        if (callback != null) {
            callback.onNotFound(name);
        }
        throw new ClassNotFoundException(name);
    }

    public interface Callback {
        void onLoadClass(ClassLoader loader, String name);

        void onNotFound(String name);
    }
}
