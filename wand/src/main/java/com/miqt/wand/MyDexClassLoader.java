package com.miqt.wand;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.util.HashMap;
import java.util.Map;

import dalvik.system.DexClassLoader;

/**
 * Created by miqt on 2018/7/9.
 */

class MyDexClassLoader extends DexClassLoader {
    private Map<String, Class<?>> classMap = new HashMap<String, Class<?>>();

    private ParentalEntrustmentLevel level = ParentalEntrustmentLevel.NEVER;

    public ParentalEntrustmentLevel getLevel() {
        return level;
    }

    public void setLevel(ParentalEntrustmentLevel level) {
        this.level = level;
    }

    public MyDexClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
        super(dexPath, optimizedDirectory, librarySearchPath, parent);
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        if (level == ParentalEntrustmentLevel.NEVER) {
            Class<?> classLoaded = classMap.get(name);
            if (classLoaded != null) {
                return classLoaded;
            }
            Class<?> findClass = null;
            try {
                findClass = findClass(name);
            } catch (Exception e) { //还可以从父类查找，这个异常吞掉，如果没有父类会抛出
            }
            if (findClass != null) {
                classMap.put(name, findClass);
                return findClass;
            }
            return super.loadClass(name);
        } else {
            return super.loadClass(name);
        }
    }

    @Override
    protected Package getPackage(String name) {
        if (ParentalEntrustmentLevel.NEVER == level) {
            return null;
        } else {
            return super.getPackage(name);
        }
    }
}
