package com.miqt.wand;

public interface Inject<T> {

    void inject(T host, Object object, Provider provider);
}
