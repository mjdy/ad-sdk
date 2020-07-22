package com.mjdy.ad.sample;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.MJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.bean.MJAdConfig;
import com.mobjump.mjadsdk.view.MJAdView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_container;

    Activity activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ll_container = findViewById(R.id.ll_container);

        activity = this;

        // 可选，获取 PHONE，STORAGE，LOCATION 权限
        MJAd.requestPermission(this);

        findViewById(R.id.btn_show_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_container.removeAllViews();

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .container(ll_container)
                        .posId("15585712")
                        .adCount(1)
                        .build();

                MJAd.showAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {

                        // 传入 container ，adViewList 必然为 null
                        LogUtil.d("ad result is null " + (adViewList == null));

                    }
                });


            }
        });

        findViewById(R.id.btn_get_ad).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .posId("51913871")
                        .adCount(1)
                        .build();

                MJAd.showAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {

                        LogUtil.d("ad result is null " + (adViewList == null));

                        if (adViewList == null) {
                            // 有可能为空
                        } else {
                            ll_container.removeAllViews();
                            ll_container.addView(adViewList.get(0));
                        }
                    }
                });


            }
        });


    }
}
