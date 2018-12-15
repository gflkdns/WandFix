package com.example.motorlib;

import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {
    static Map<Class, String> holder = new HashMap<>();

    public static <T> T make(Class<T> tClass) {
        try {
            Class<T> ap = (Class<T>) Motor.get()
                    .getClassLoader().loadClass(holder.get(tClass));
            T o = ap.newInstance();
            return o;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
