package com.miqt.wand;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by t54 on 2018/12/18.
 */

public interface Encrypter {
    void encrypt(OutputStream out, InputStream in);

    void decrypt(OutputStream out, InputStream in);
}
