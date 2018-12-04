package com.mjdy.ad.sample;

import android.app.Application;

import com.mobjump.mjadsdk.MJAd;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MJAd.init(this, "2");  // init ad sdk
    }
}
