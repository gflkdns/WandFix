package com.miqt.wand;


import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.util.Map;

public class ObjectProvider implements Provider {

    @Override
    public Object make(Object object, String classname, ParentalEntrustmentLevel level) {
        Map<String, Object[]> map = (Map<String, Object[]>) object;
        if (map == null || map.get(classname) == null) {
            return ObjectFactory.make(classname, level);
        }
        return ObjectFactory.make(classname, level, map.get(classname));
    }
}
