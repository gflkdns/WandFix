package com.miqt.wand.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Specify the class name of the injected object,This class can be loaded externally
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.FIELD})
public @interface InjectObject {
    //class name
    String className();

    //双亲委托禁用级别
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
