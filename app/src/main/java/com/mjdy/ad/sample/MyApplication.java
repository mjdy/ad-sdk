package com.mjdy.ad.sample;

import android.app.Application;

import com.mobjump.mjadsdk.MJAd;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化广告sdk，最好放在程序入口，比如application里
        MJAd.init(this, "1KnLA300");  // init ad sdk

//        MJAd.init(this,"appId","oaid","channel"); // android 10 need this

    }
}
