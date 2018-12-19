package com.miqt.wand.anno;

public @interface InjectObject {
    //class name
    String value();

    //双亲委托禁用级别
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
