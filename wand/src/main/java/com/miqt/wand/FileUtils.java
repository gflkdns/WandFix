package com.miqt.wand;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by t54 on 2018/12/18.
 */

public class FileUtils {
    public static boolean copyFileFromAssets(Encrypter encrypter, Context context, String assetName, String path) {
        boolean bRet = false;
        try {
            InputStream is = context.getAssets().open(assetName);

            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            if (encrypter != null) {
                encrypter.decrypt(fos, is);
            } else {
                byte[] temp = new byte[64];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }

    public static boolean copyFile(Encrypter encrypter, Context context, String from, String to) {
        boolean bRet = false;
        try {
            FileInputStream is = new FileInputStream(from);
            File file = new File(to);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            if (encrypter != null) {
                encrypter.decrypt(fos, is);
            } else {
                byte[] temp = new byte[64];
                int i = 0;
                while ((i = is.read(temp)) > 0) {
                    fos.write(temp, 0, i);
                }
            }
            if (fos != null) {
                fos.close();
            }
            if (is != null) {
                is.close();
            }
            bRet = true;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bRet;
    }
}
