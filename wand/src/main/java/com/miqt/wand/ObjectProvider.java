package com.miqt.wand;


public class ObjectProvider implements Provider {

    @Override
    public Object make(Object object, String classname) {
        try {
            Class ap = Wand.get()
                    .getClassLoader().loadClass(classname);
            Object o = ap.newInstance();
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
