package com.example.miqt.dexmvppdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.motorlib.AppParsenter;
import com.miqt.wand.ClassInstall;
import com.miqt.wand.Wand;
import com.miqt.wand.anno.InjectObject;
import com.miqt.wand.anno.ParentalEntrustmentLevel;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @InjectObject(
            value = "com.example.motordex.AppParsenterImpl",
            level = ParentalEntrustmentLevel.NEVER)
    AppParsenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Wand.with(this).init("hotfix_pack.dex");
        ClassInstall.inject(this);
    }

    public void getStr(View view) {
        String str = ap.getStr();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
