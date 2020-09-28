package com.mjdy.ad.sample;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.MJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.bean.MJAdConfig;
import com.mobjump.mjadsdk.view.MJAdView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    LinearLayout ll_container;
    Activity activity;


    MJAdView mjAdView;

    TextView tv_status;

    public static final String POS_ID = "15585712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ll_container = findViewById(R.id.ll_container);
        tv_status = findViewById(R.id.tv_status);

        activity = this;

        // 可选，获取 PHONE，STORAGE，LOCATION 权限
        MJAd.requestPermission(this);

        // 带有container的示例。
        findViewById(R.id.btn_ad_container).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_container.removeAllViews();
                ll_container.setVisibility(View.GONE);
                tv_status.setText("广告状态：带有container ，交给sdk处理");

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .container(ll_container)
                        .posId(POS_ID)
                        .adCount(1)
                        .build();

                MJAd.showAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {
                        // 传入 container ，adViewList 必然为 null
                        LogUtil.d("ad result is null " + (adViewList == null));

//                        ll_container.setVisibility(View.VISIBLE);  sdk会自动将container 设为可见状态

                        tv_status.setText("广告状态：带有container ，交给sdk处理 , adViewList is null " + (adViewList == null));
                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        super.onAdLoadFail(errorModel);
                        tv_status.setText("广告状态： 加载失败 " + errorModel.toString());

                    }
                });

            }
        });


        // 不带container ，预加载

        findViewById(R.id.btn_ad_load).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ll_container.setVisibility(View.GONE);
                tv_status.setText("广告状态： 加载中...");

                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .posId(POS_ID)
                        .adCount(1)
                        .build();

                MJAd.showAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {
                        // 预加载模式，adViewList大概率有值，不过仍建议进行非空判断

                        if (adViewList != null && adViewList.size() > 0) {
                            mjAdView = adViewList.get(0);
                            tv_status.setText("广告状态： 预加载成功，可以点击显示");
                        } else {
                            tv_status.setText("广告状态： 几乎不可能的状态");
                        }
                        LogUtil.d("ad result is null " + (adViewList == null));


                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {
                        super.onAdLoadFail(errorModel);

                        tv_status.setText("广告状态： 加载失败 " + errorModel.toString());


                    }
                });


            }
        });

        findViewById(R.id.btn_ad_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mjAdView == null) {
                    Toast.makeText(MainActivity.this, "请等待广告加载", Toast.LENGTH_SHORT).show();
                    return;
                }
                // show方法里会 removeAllViews() & setVisibility(View.VISIBLE)
                mjAdView.show(ll_container);

            }
        });


    }
}
