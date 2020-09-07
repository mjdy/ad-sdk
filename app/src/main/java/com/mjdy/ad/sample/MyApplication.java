package com.mjdy.ad.sample;

import android.app.Application;

import com.mobjump.mjadsdk.MJAd;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化广告sdk，最好放在程序入口，比如application里
        MJAd.init(this, "1KnLA300");  // init ad sdk


        // OAID 尽量赋值. 以下两种方式均可

        // 方式一
//        MJAd.init(this,"appId","oaid","channel");

        // 方式二
//        MJAd.setOaid("oaid");
//        MJAd.setChannel("channel");

    }
}
