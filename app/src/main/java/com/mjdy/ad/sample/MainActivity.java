package com.mjdy.ad.sample;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.view.MJBannerView;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_banner;

    Activity activity;
    MJBannerView banner_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ll_banner = findViewById(R.id.ll_bannder);

        activity = this;

        // need request permission
        // 当选择 广点通 or 百度广告 时，需要赋予以下两个权限，否则可能会导致广告无法显示
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);

        // show splash AD  开屏广告
        findViewById(R.id.btn_splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MJAd.showSplashAd(activity, "2", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {
                    }

                    @Override
                    public void onAdLoadFail(String failCode) {
                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });


        // show Interstitial AD  插屏广告
        findViewById(R.id.btn_interteristal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MJAd.showInterstitialAd(activity, "3", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(String i) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });


        // show Banner AD
        banner_view = findViewById(R.id.banner_view);
        banner_view.setRefreshTime(10);
        findViewById(R.id.btn_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MJAd.showBannerAd(activity, banner_view, "4", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(String failCode) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
            }
        });


        // show Feed AD  信息流广告
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsListActivity.launch(activity);
            }
        });


        // show float AD , To be continue...   悬浮窗广告

        findViewById(R.id.btn_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Will Come Soon ...", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
