package com.mjdy.ad.sample;

import android.app.Application;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.bean.MJSdkConfig;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化广告sdk，最好放在程序入口，比如application里
        MJAd.init(this, "1KnLA300");  // init ad sdk


        // 如果需要设置channel 以及 隐私权限配置，用下面的初始化
//        MJSdkConfig mjSdkConfig = new MJSdkConfig();
//        mjSdkConfig.setChannel("yourChannel");
//
//        // 权限控制。1是允许，0是拒绝，不设置是默认
//        mjSdkConfig.setCanUsePhoneState(1); // 手机信息，如imei
//        mjSdkConfig.setCanUseLocation(1);  // 位置信息
//        mjSdkConfig.setCanUseStorage(1); // 存储权限
//        mjSdkConfig.setCanUseAndroidId(1); // androidId
//        mjSdkConfig.setCanUseAppList(1); // app列表
//        mjSdkConfig.setCanUseIp(1); // ip
//        mjSdkConfig.setCanUseMacAddress(0); // mac地址
//        mjSdkConfig.setCanUseWifiState(0); // wifi状态
//        mjSdkConfig.setCanUseLimitPersonalAds(0); // 个性化推荐
//        mjSdkConfig.setCanUseProgramRecommend(0); // 程序化推荐
//
//        // 以上有设置为0的，建议主动设置下面对应值
//        mjSdkConfig.setImei("00000000000000"); // 主动设置imei
//        mjSdkConfig.setOaid("000000000000000"); // 主动设置oaid
//        mjSdkConfig.setAndroidId("01234567"); // 主动设置androidId
//        mjSdkConfig.setMacAddress("00.00.00.00.00"); //主动设置mac
//        mjSdkConfig.setIp("1.1.1.1"); // 主动设置ip
//
//        MJAd.init(this, "appId", mjSdkConfig); // if  have channel ,use this

    }
}
