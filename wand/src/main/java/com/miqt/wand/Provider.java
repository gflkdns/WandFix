package com.miqt.wand;

public interface Provider {
    Object make(Object object, String classname);
}
