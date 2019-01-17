package com.example.miqt.dexmvppdemo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.miqt.wand.ActivityDespacher;
import com.miqt.wand.ClassInstall;
import com.miqt.wand.Wand;
import com.miqt.wand.anno.InjectObject;
import com.miqt.wand.anno.ParentalEntrustmentLevel;

/**
 * Created by t54 on 2019/1/17.
 */

public class BaseActivity extends AppCompatActivity {
    @InjectObject(value = "com.miqt.wand.ActivityDespacher",
            level = ParentalEntrustmentLevel.NEVER)
    ActivityDespacher despacher;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        ClassInstall.inject(this);
        if (despacher.onDespacher(this)) {
            finish();
        }
    }
}
