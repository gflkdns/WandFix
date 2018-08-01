package com.example.motorlib;

/**
 * Created by miqt on 2018/7/23.
 */

public class ObjectFactory {
    public static Object make(String type) {
        try {
            Class<AppParsenter> ap = (Class<AppParsenter>) Motor.get()
                    .getClassLoader().loadClass("com.example.motordex.AppParsenterImpl");
            AppParsenter o = ap.newInstance();
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
