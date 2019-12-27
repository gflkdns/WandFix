package com.miqt.wand.bean;

import com.miqt.wand.Wand;
import com.miqt.wand.utils.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class DexPatch {
    private String dexFilePath;
    private String cacheFilePath;

    public DexPatch(String dexFilePath, String cacheFilePath) throws FileNotFoundException {
        this.dexFilePath = dexFilePath;
        this.cacheFilePath = cacheFilePath;

        verificationPath(dexFilePath, cacheFilePath);
    }

    private void verificationPath(String dexFilePath, String cacheFilePath) throws FileNotFoundException {
        File dexFile = new File(dexFilePath), cacheDir = new File(cacheFilePath);
        if (!dexFile.exists() || dexFile.length() <= 0) {
            throw new FileNotFoundException("dex文件不存在");
        }
        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }
        //dex文件对比缓存文件夹不可以相同
        if (dexFile.getParent().equals(cacheDir.getAbsolutePath())) {
            throw new FileNotFoundException("dex文件对比缓存文件夹不可以相同");
        }

        String path = Wand.get().getContext().getCacheDir().getParent();
        if (!dexFile.getAbsolutePath().contains(path) || !cacheDir.getAbsolutePath().contains(path)) {
            throw new FileNotFoundException("dex文件和缓存文件夹只能存在沙盒目录中");
        }
    }

    public String getDexFilePath() {
        return dexFilePath;
    }

    public void setDexFilePath(String dexFilePath) {
        this.dexFilePath = dexFilePath;
    }

    public String getCacheFilePath() {
        return cacheFilePath;
    }

    public void setCacheFilePath(String cacheFilePath) {
        this.cacheFilePath = cacheFilePath;
    }

    public void delete() {
        FileUtils.deleteFile(new File(cacheFilePath));
        FileUtils.deleteFile(new File(dexFilePath));
    }
}
