package com.miqt.wand;

import java.nio.ByteBuffer;

/**
 * 热修复包解码器
 *
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:35:31
 */
public interface Encrypter {
    byte[] encrypt(ByteBuffer data);

    void decrypt(ByteBuffer data);
}
