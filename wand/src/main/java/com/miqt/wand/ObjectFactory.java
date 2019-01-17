package com.miqt.wand;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectFactory {

    public static <T> T invokeStaticMethod(String classname, String methodName,
                                           ParentalEntrustmentLevel level, Object... pram) {
        return invokeMethod(null, classname, methodName, level, pram);
    }

    public static <T> T invokeMethod(Object object, String classname, String methodName,
                                     ParentalEntrustmentLevel level, Object... pram) {
        try {
            Class<T> ap = (Class<T>) Wand.get().loadClass(classname, level);
            T o = null;
            if (pram.length == 0) {
                Method method = ap.getDeclaredMethod(methodName);
                method.setAccessible(true);
                o = (T) method.invoke(object);
            } else {
                Class[] aClass = new Class[pram.length];
                for (int i = 0; i < aClass.length; i++) {
                    aClass[i] = pram[i].getClass();
                }
                Method method = ap.getDeclaredMethod(methodName, aClass);
                method.setAccessible(true);
                o = (T) method.invoke(object, pram);
            }
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T make(String classname, Object... pram) {
        return make(classname, ParentalEntrustmentLevel.NEVER);
    }

    public static <T> T make(String classname, ParentalEntrustmentLevel level, Object... pram) {
        try {
            Class<T> ap = (Class<T>) Wand.get().loadClass(classname, level);
            T o = null;
            if (pram.length == 0) {
                Constructor<T> constructor = ap.getDeclaredConstructor();
                constructor.setAccessible(true);
                o = constructor.newInstance();
            } else {
                Class[] aClass = new Class[pram.length];
                for (int i = 0; i < aClass.length; i++) {
                    aClass[i] = pram[i].getClass();
                }
                Constructor<T> constructor = ap.getDeclaredConstructor(aClass);
                constructor.setAccessible(true);
                o = constructor.newInstance(pram);
            }
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }
}
