package com.example.miqt.dexmvppdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.motorlib.AppParsenter;
import com.example.motorlib.ObjectFactory;
import com.example.motorlib.Motor;

public class MainActivity extends AppCompatActivity {
    AppParsenter ap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Motor.init(this, new Motor.MotorListener() {
            @Override
            public void initFnish() {
                ap = (AppParsenter) ObjectFactory.make(MainActivity.class.getName());
            }
            @Override
            public void initError(Throwable throwable) {
            }
        });

    }

    public void getStr(View view) {
        String str = ap.getStr();
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }
}
