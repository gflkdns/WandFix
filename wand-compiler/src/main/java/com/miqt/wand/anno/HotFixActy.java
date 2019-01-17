package com.miqt.wand.anno;


public @interface HotFixActy {
    ParentalEntrustmentLevel level()
            default ParentalEntrustmentLevel.NEVER;
}
