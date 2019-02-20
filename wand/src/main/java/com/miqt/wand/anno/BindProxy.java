package com.miqt.wand.anno;

/**
 * Created by t54 on 2019/2/20.
 */

public @interface BindProxy {
    //class name
    Class clazz();

    //双亲委托禁用级别
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
