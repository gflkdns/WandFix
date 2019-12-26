package com.miqt.wand;

import java.io.File;

/**
 * 热修复包解码器
 *
 * @author https://github.com/miqt/WandFix
 * @time 2018年12月19日17:35:31
 */
public interface Encrypter {
    void encrypt(File from, File to);

    void decrypt(File from, File to);
}
