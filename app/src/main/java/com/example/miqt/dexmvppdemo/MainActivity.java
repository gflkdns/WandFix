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
            level = ParentalEntrustmentLevel.PROJECT)
    AppParsenter ap;
    @InjectObject(
            value = "com.example.motordex.AppParsenterImpl",
            level = ParentalEntrustmentLevel.NEVER)
    AppParsenter ap2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Wand.with(this).init();
        Map<String, Object[]> map = new HashMap<>();
        map.put("com.example.motordex.AppParsenterImpl", new Object[]{1, "参数2", "参数3"});
        ClassInstall.inject(this, map);
    }

    public void getStr(View view) {
        String str = ap.getStr();
        String str2 = str + "--" + ap2.getStr();
        Toast.makeText(this, str2, Toast.LENGTH_SHORT).show();
    }
}
