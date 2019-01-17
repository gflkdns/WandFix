package com.miqt.wand;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;

import java.util.concurrent.ConcurrentHashMap;

public class ActivityDespacher {
    private static ConcurrentHashMap<String, Boolean> map = new ConcurrentHashMap<>();

    public boolean onDespacher(Activity activity) {
        String name = activity.getClass().getName();
        if (map.containsKey(name) && map.get(name)) {
            return false;
        }
        map.put(name, true);
        Intent intent = new Intent(activity.getIntent());
        intent.setComponent(
                new ComponentName(activity, activity.getClass().getName() + "$$HotFix")
        );
        activity.startActivity(intent);
        return true;
    }
}
