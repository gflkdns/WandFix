package com.miqt.wand;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:35:31
 */
public interface Encrypter {
    void encrypt(OutputStream out, InputStream in);

    void decrypt(OutputStream out, InputStream in);
}
