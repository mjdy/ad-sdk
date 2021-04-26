package com.mjdy.ad.sample.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.mjdy.ad.sample.utils.LogUtil;
import com.mjdy.ad.sample.R;
import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.MJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.bean.MJAdConfig;
import com.mobjump.mjadsdk.view.MJAdView;

import java.util.List;

/**
 * sdk处理显示广告，获取广告即显示，需要app层提供显示广告所用的容器
 */
public class SdkHandleActivity extends AppCompatActivity {

    LinearLayout ll_container;
    Activity activity;
    TextView tv_info;

    public static final String POS_ID = "15585712";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sdk_handle_activity);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ll_container = findViewById(R.id.ll_container);
        tv_info = findViewById(R.id.tv_info);

        activity = this;

        findViewById(R.id.btn_show).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });
    }

    private void showAd() {

        ll_container.setVisibility(View.GONE); // 默认隐藏
        tv_info.setText("交由sdk处理，请求广告");


        // .container 传入容器。sdk获取到广告后，立即显示。即把广告放进传入的容器里
        MJAdConfig adConfig = new MJAdConfig.Builder()
                .activity(activity)
                .container(ll_container)
                .posId(POS_ID)
                .build();

        // 只有 onAdLoadSuccess 是必须实现的回调，其他根据需求可选
        MJAd.showAd(adConfig, new MJAdListener() {
            @Override
            public void onAdLoadSuccess(List<MJAdView> adViewList) {
                // 传入 container ，adViewList 必然为 null 。
                LogUtil.d("ad result is null " + (adViewList == null));
//              ll_container.setVisibility(View.VISIBLE);  sdk会自动将container 设为可见状态. 无需app层处理
            }

            @Override
            public void onAdLoadFail(ErrorModel errorModel) {
                super.onAdLoadFail(errorModel);
                tv_info.setText("广告状态： 加载失败 " + errorModel.toString());
            }

            @Override
            public void onAdShow() {
                super.onAdShow();
                LogUtil.d("ad result show");
                tv_info.setText("广告状态： 显示 ");
            }

            @Override
            public void onAdClicked() {
                super.onAdClicked();
                LogUtil.d("ad result click");
                tv_info.setText("广告状态： 点击 ");
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}
