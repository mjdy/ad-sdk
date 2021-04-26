package com.mjdy.ad.sample.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mjdy.ad.sample.R;
import com.mobjump.mjadsdk.MJAd;

public class MainActivity extends AppCompatActivity {

    TextView tv_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        findViewById(R.id.btn_sdk_handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // sdk处理显示广告，获取广告即显示，需要app层提供显示广告所用的容器
                startActivity(new Intent(MainActivity.this, SdkHandleActivity.class));
            }
        });

        findViewById(R.id.btn_app_handle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // app层处理显示广告，预加载
                startActivity(new Intent(MainActivity.this, AppHandleActivity.class));
            }
        });


        // 可选，获取 PHONE，STORAGE，LOCATION 权限
        MJAd.requestPermission(this);

    }
}
