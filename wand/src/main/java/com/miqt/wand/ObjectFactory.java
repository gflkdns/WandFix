package com.miqt.wand;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class ObjectFactory {
    private final static HashMap<Class, String> mapMemberClass = new HashMap<Class, String>(8);

    static {
        mapMemberClass.put(Integer.class, "int");
        mapMemberClass.put(Double.class, "double");
        mapMemberClass.put(Float.class, "float");
        mapMemberClass.put(Character.class, "char");
        mapMemberClass.put(Boolean.class, "boolean");
        mapMemberClass.put(Short.class, "short");
        mapMemberClass.put(Long.class, "long");
        mapMemberClass.put(Byte.class, "byte");
    }

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
            Constructor<T>[] constructors = (Constructor<T>[]) ap.getDeclaredConstructors();
            Constructor<T> constructor = null;
            for (int i = 0; i < constructors.length; i++) {
                Class[] aClass = constructors[i].getParameterTypes();
                if (aClass.length != pram.length) {
                    continue;
                }
                //识别是不是正确的构造方法
                boolean found = true;
                for (int j = 0; j < aClass.length; j++) {
                    List<String> baseClassList = getBaseClass(pram[j].getClass());
                    if (!baseClassList.contains(aClass[j].getName())) {
                        found = false;
                    }
                }
                if (found) {
                    constructor = constructors[i];
                    break;
                }
            }
            if (constructor == null) {
                throw new IllegalArgumentException("[" + classname + "]" + "not has parameter type constructor");
            }
            constructor.setAccessible(true);
            T o = constructor.newInstance(pram);
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static List<String> getBaseClass(Class clazz) {
        List<String> result = new LinkedList<>();
        result.addAll(getSuperClass(clazz));
        result.addAll(getInterfaces(clazz));
        return result;
    }

    private static List<String> getInterfaces(Class clazz) {
        List<String> result = new LinkedList<>();
        if (clazz == null) {
            return result;
        }
        Class[] classes = clazz.getInterfaces();
        for (int i = 0; i < classes.length; i++) {
            result.add(classes[i].getName());
        }
        for (int i = 0; i < classes.length; i++) {
            result.addAll(getInterfaces(classes[i]));
        }
        return result;
    }

    private static List<String> getSuperClass(Class clazz) {
        List<String> result = new LinkedList<>();
        while (clazz != null) {
            String name = mapMemberClass.get(clazz);
            if (name != null) {
                result.add(name);
            }
            result.add(clazz.getName());
            clazz = clazz.getSuperclass();
        }
        return result;
    }
}
