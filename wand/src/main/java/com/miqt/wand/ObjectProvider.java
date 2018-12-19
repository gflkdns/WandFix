package com.miqt.wand;


import java.util.Map;

public class ObjectProvider implements Provider {

    @Override
    public Object make(Object object, String classname) {
        Map<String, Object[]> map = (Map<String, Object[]>) object;
        if (map == null || map.get(classname) == null) {
            return ObjectFactory.make(classname);
        }
        return ObjectFactory.make(classname, map.get(classname));
    }
}
