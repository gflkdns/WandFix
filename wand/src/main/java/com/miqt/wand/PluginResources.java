package com.miqt.wand;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.miqt.wand.anno.ParentalEntrustmentLevel;

/**
 * Created by miqt on 2019/2/21.
 * 获取插件apk中的布局，图片，颜色，尺寸等资源
 */
public class PluginResources {
    AssetManager mManager;
    Resources mResources;
    private String packageName;
    Context context;

    /**
     * @param context 上下文
     * @param apkPath 插件apk路径
     */
    public PluginResources(@NonNull Context context, @NonNull String apkPath) {
        this.context = context;
        init(apkPath);
    }

    private void init(String apkPath) {
        mManager = ObjectFactory.make(AssetManager.class);
        int cookie = ObjectFactory.invokeMethod(mManager, AssetManager.class.getName(),
                "addAssetPath", ParentalEntrustmentLevel.PROJECT, apkPath);
        mResources = new Resources(mManager,
                context.getResources().getDisplayMetrics(),
                context.getResources().getConfiguration());
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info = packageManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
            packageName = appInfo.packageName;
        }
    }

    public Resources getResources() {
        return mResources;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getId(String name, String type) {
        return mResources.getIdentifier(name, type, packageName);
    }

    public View getLayout(String name) {
        XmlResourceParser parser = mResources.getLayout(getId(name, "layout"));
        return LayoutInflater.from(context).inflate(parser, null, false);
    }
}
