package com.miqt.wand.bean;

import com.miqt.wand.utils.FileUtils;

import java.io.File;

public class DexPatch {
    private String dexFilePath;
    private String cacheFilePath;

    public DexPatch(String dexFilePath, String cacheFilePath) {
        this.dexFilePath = dexFilePath;
        this.cacheFilePath = cacheFilePath;
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
