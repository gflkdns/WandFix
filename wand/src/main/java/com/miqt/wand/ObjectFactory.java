package com.miqt.wand;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
            Class[] aClass = null;
            Constructor<?>[] constructors = ap.getDeclaredConstructors();
            for (int i = 0; i < constructors.length; i++) {
                aClass = constructors[i].getParameterTypes();
                if (aClass.length != pram.length) {
                    continue;
                }
                //识别是不是正确的构造方法
                for (int j = 0; j < aClass.length; j++) {
                    List<Class> baseClassList = getBaseClass(pram[i].getClass());
                    if (!baseClassList.contains(aClass[i])) {
                        aClass = null;
                        break;
                    }
                }
            }
            Constructor<T> constructor = ap.getDeclaredConstructor(aClass);
            constructor.setAccessible(true);
            T o = constructor.newInstance(pram);
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

    private static List<Class> getBaseClass(Class clazz) {
        List<Class> result = new LinkedList<>();
        result.addAll(getSuperClass(clazz));
        result.addAll(getInterfaces(clazz));
        return result;
    }

    private static List<Class> getInterfaces(Class clazz) {
        List<Class> result = new LinkedList<>();
        if (clazz == null) {
            return result;
        }
        Class[] classes = clazz.getInterfaces();
        result.addAll(Arrays.asList(classes));
        for (int i = 0; i < classes.length; i++) {
            result.addAll(getInterfaces(classes[i]));
        }
        return result;
    }

    private static List<Class> getSuperClass(Class clazz) {
        List<Class> result = new LinkedList<>();
        while (clazz != null) {
            result.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return result;
    }
}
