package com.miqt.wand.anno;


public @interface HotFixActy {
    /**
     * 双亲委托禁用级别
     * 本地开发期间一般用ParentalEntrustmentLevel.PROJECT
     * 发布之后一般使用ParentalEntrustmentLevel.NEVER
     */
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
