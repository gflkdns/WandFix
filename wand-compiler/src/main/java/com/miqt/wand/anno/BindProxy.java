package com.miqt.wand.anno;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by miqt on 2019/2/20.
 */
@Documented
@Retention(RUNTIME)
@Target({ElementType.TYPE})
public @interface BindProxy {
    //class name
    Class clazz();

    //双亲委托禁用级别
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
