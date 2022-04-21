package com.mjdy.ad.sample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mjdy.ad.sample.utils.LogUtil;
import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.MJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.bean.MJAdConfig;
import com.mobjump.mjadsdk.view.MJAdView;

import java.util.List;

public class MainActivity extends Activity {
    LinearLayout ll_container;
    Activity activity;
    MJAdView mjAdView;
    TextView tv_info;


//    public static final String POS_ID = "40663297"; // 快手
//    public static final String POS_ID = "77894185"; // GroMore
//    public static final String POS_ID = "95041688"; // 优量汇
    public static final String POS_ID = "15585712"; // 随机

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_activity);

        // 可选，获取 PHONE，STORAGE，LOCATION 权限
        MJAd.requestPermission(this);

        ll_container = findViewById(R.id.ll_container);
        tv_info = findViewById(R.id.tv_info);

        activity = this;

        // 简单预加载
        findViewById(R.id.btn_ad_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_container.setVisibility(View.GONE);
                tv_info.setText("广告状态： 加载中...");

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .posId(POS_ID)
                        .build();

                MJAd.loadAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {
                        // 预加载模式，adViewList大概率有值，不过仍建议进行非空判断

                        if (adViewList != null && adViewList.size() > 0) {
                            mjAdView = adViewList.get(0);
                            tv_info.setText("广告状态： 预加载成功，可以点击显示");
                        } else {
                            tv_info.setText("广告状态： 几乎不可能的状态");
                        }
                        LogUtil.d("ad result is null " + (adViewList == null));


                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        super.onAdLoadFail(errorModel);

                        tv_info.setText("广告状态： 加载失败 " + errorModel.toString());


                    }
                });


            }
        });


        // app自定义布局的预加载
        findViewById(R.id.btn_ad_load_custom).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_container.setVisibility(View.GONE);
                tv_info.setText("广告状态： 加载中...");

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .posId(POS_ID)
                        .layout(R.layout.fm_ad_custom_template) // 传入layout，自定义布局。里面的id不要动
                        .build();

                MJAd.loadAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {
                        // 预加载模式，adViewList大概率有值，不过仍建议进行非空判断

                        if (adViewList != null && adViewList.size() > 0) {
                            mjAdView = adViewList.get(0);
                            tv_info.setText("广告状态： 预加载成功，可以点击显示");
                        } else {
                            tv_info.setText("广告状态： 几乎不可能的状态");
                        }
                        LogUtil.d("ad result is null " + (adViewList == null));

                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        super.onAdLoadFail(errorModel);

                        tv_info.setText("广告状态： 加载失败 " + errorModel.toString());

                    }


                    @Override
                    public void onAdCustomView(View view) {
                        super.onAdCustomView(view);
                        //  这个方法会将用户传入的 .layout(R.layout.fm_ad_custom_template) 返回回来。可以通过 view.findViewById 拿到对应布局
                        TextView tv_custom_text = view.findViewById(R.id.tv_custom_text);
                        tv_custom_text.setText("这是app层自定义的view");
                    }
                });


            }
        });


        findViewById(R.id.btn_ad_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mjAdView == null) {
                    tv_info.setText("请等待广告加载");
                    Toast.makeText(MainActivity.this, "请等待广告加载", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 在需要的时机调用show ，传入展示用的容器
                mjAdView.show(ll_container);

            }
        });


    }

}
