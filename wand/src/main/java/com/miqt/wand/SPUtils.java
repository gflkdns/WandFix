package com.miqt.wand;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by miqt on 2018/12/26.
 */

public class SPUtils {
    private static final String SP_NAME = "wand_sp";

    public static String get(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void put(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.putString(key, value);
        editor.apply();
    }
}
