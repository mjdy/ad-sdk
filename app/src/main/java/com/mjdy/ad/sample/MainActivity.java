package com.mjdy.ad.sample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.bean.FloadAdConfig;
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
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);

        // show splash AD
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


        // show Interstitial AD
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


        // show Feed AD
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NewsListActivity.launch(activity);
            }
        });


        // show float AD , To be continue...

        findViewById(R.id.btn_float).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "Will Come Soon ...", Toast.LENGTH_SHORT).show();

            }
        });


    }
}
