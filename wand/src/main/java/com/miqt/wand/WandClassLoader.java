package com.miqt.wand;

import java.util.HashSet;

import dalvik.system.DexClassLoader;

/**
 * Created by miqt on 2018/7/9.
 * 先自己找，自己找不到父亲找，父亲找不到孩子找
 */

class WandClassLoader extends DexClassLoader {
    /**
     * 用来回调一个类具体是由谁加载了
     */
    private Callback callback;
    //记录自己找不到的类
    private HashSet<String> notFoundClass;

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
            //向下双亲委派
            throw new ClassNotFoundException(name);
        }
        //-------自己缓存里面找
        Class<?> c = findLoadedClass(name);
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
        //--------给孩子找，孩子会继续双亲委托拜托给我，因此我记下来这个类是给孩子找的，拜托我的时候，直接告诉他找不到
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
