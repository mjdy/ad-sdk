package com.mjdy.ad.sample;

import android.app.Application;

import com.mobjump.mjadsdk.MJAd;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化广告sdk，最好放在程序入口，比如application里
        MJAd.init(this, "1KnLA300");  // init ad sdk

        // 如果需要设置channel，用下面这个初始化
//        MJAd.init(this,"appId", "yourChannel"); // if  have channel ,use this

    }
}
