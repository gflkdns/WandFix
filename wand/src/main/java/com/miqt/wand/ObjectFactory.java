package com.miqt.wand;

import java.util.HashMap;
import java.util.Map;

public class ObjectFactory {
    static Map<Class, String> holder = new HashMap<>();

    public static <T> T make(String classname) {
        try {
            Class<T> ap = (Class<T>) Wand.get()
                    .getClassLoader().loadClass("com.example.motordex.AppParsenterImpl");
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
