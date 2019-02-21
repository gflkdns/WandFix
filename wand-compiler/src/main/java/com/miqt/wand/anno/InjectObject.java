package com.miqt.wand.anno;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
public @interface InjectObject {
    //class name
    String className();

    //双亲委托禁用级别
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
