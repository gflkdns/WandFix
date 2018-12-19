package com.miqt.wand;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

public interface Provider {
    Object make(Object object, String classname, ParentalEntrustmentLevel level);
}
