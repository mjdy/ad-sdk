package com.mjdy.ad.sample;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.adline.interfaces.OnMJRewadVideoListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.bean.RewardModel;
import com.mobjump.mjadsdk.view.MJBannerView;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_banner;

    Activity activity;
    MJBannerView banner_view;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ll_banner = findViewById(R.id.ll_banner);

        activity = this;

        // need request permission
        // 当选择 广点通 or 百度广告 时，需要赋予以下两个权限，否则可能会导致广告无法显示
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1234);

        // show splash AD  开屏广告
        findViewById(R.id.btn_splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MJAd.showSplashAd(activity, "99309152", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {
                        LogUtil.d("splash ad success");
                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        LogUtil.d("splash ad fail " + errorModel);
                    }

                    @Override
                    public void onAdClicked() {
                        LogUtil.d("splash ad click ");
                    }

                    @Override
                    public void onAdDismiss() {
                        LogUtil.d("splash ad dismiss");

                    }
                });
            }
        });


        // show Interstitial AD  插屏广告
        findViewById(R.id.btn_interteristal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MJAd.showInterstitialAd(activity, "20886925", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {
                        LogUtil.d("interteristal ad success");
                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        LogUtil.d("interteristal ad fail " + errorModel);

                    }

                    @Override
                    public void onAdClicked() {
                        LogUtil.d("interteristal ad click");

                    }

                    @Override
                    public void onAdDismiss() {
                        LogUtil.d("interteristal ad dismiss");

                    }
                });
            }
        });


        // show Banner AD
        banner_view = findViewById(R.id.banner_view);
        banner_view.setRefreshTime(20);
        findViewById(R.id.btn_banner).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MJAd.showBannerAd(activity, banner_view, "96743842", new OnMJAdListener() {
                    @Override
                    public void onAdLoadSuccess() {
                        LogUtil.d("banner ad success");

                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        LogUtil.d("banner ad fail " + errorModel);

                    }

                    @Override
                    public void onAdClicked() {
                        LogUtil.d("banner ad click");

                    }

                    @Override
                    public void onAdDismiss() {
                        LogUtil.d("banner ad dismiss");

                    }
                });
            }
        });


        findViewById(R.id.btn_reward_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO
//                MJAd.showVideoRewardAd(activity, "6", new OnMJRewadVideoListener() {
//
//
//                    @Override
//                    public void onVideoPlayFinish() {
//                        LogUtil.d("onVideoPlayFinish");
//                    }
//
//                    @Override
//                    public void onReward(RewardModel rewardModel) {
//                        LogUtil.d("onReward  name : " + rewardModel.name + " , amount: " + rewardModel.amount);
//                    }
//
//                    @Override
//                    public void onAdLoadSuccess() {
//                        LogUtil.d("onAdLoadSuccess");
//                    }
//
//                    @Override
//                    public void onAdLoadFail(ErrorModel errorModel) {
//                        LogUtil.d("onAdLoadFail "+errorModel.toString());
//                    }
//
//                    @Override
//                    public void onAdClicked() {
//                        LogUtil.d("onAdClicked");
//                    }
//
//                    @Override
//                    public void onAdDismiss() {
//                        LogUtil.d("onAdDismiss");
//                    }
//                });
            }
        });


        // show Feed AD  信息流广告
        findViewById(R.id.btn_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedListActivity.launch(activity);
            }
        });

        // show Feed AD  第三方信息流广告
        findViewById(R.id.btn_third_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedListThirdAdapterActivity.launch(activity);
            }
        });

        // show Feed AD  自定义信息流广告
        findViewById(R.id.btn_custom_list).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FeedListCustomActivity.launch(activity);
            }
        });

    }
}
