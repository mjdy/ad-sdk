package com.mjdy.ad.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.view.FeedAdOnly;
import com.mobjump.mjadsdk.view.MJFeedAdView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 11/16/18.
 * 信息流广告演示demo
 * 仅提供adViews，开发者获取后自行插入
 */
public class FeedListCustomActivity extends Activity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, FeedListCustomActivity.class));
    }

    RecyclerView rv_content;  // 目前仅支持 RecyclerView

    ArrayList<CustomModel> data = new ArrayList<>();  // 需要显示的数据

    NormalCustomAdapter adapter;  // 开发者自己的adapter


    FeedAdOnly feedAdOnly; // 获取ads的工具类

    private int lastAdInsertPosition; // 上一次插入广告的位置

    public static final int AD_NUM_PER_PAGE = 3; // 每页需要显示的数据条数。demo里每页20条，也就是每20条数据，插入3条广告

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_list_activity);
        rv_content = findViewById(R.id.rv_content);

        findViewById(R.id.btn_load_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载更多
                ArrayList<CustomModel> newList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    CustomModel customModel = new CustomModel();
                    customModel.text = "load more  " + i;
                    newList.add(customModel);
                }

                data.addAll(newList);
                // when list changed 数据更改时要调用此方法刷新数据
                adapter.notifyDataSetChanged();

                feedAdOnly.refresh(AD_NUM_PER_PAGE);

            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 模拟下拉刷新

                ArrayList<CustomModel> newList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    CustomModel customModel = new CustomModel();
                    customModel.text = "refresh " + i;
                    newList.add(customModel);
                }

                data.clear();
                data.addAll(newList);

                adapter.notifyDataSetChanged();

                // 需要显示的数据更新完，去请求广告

                lastAdInsertPosition = 0; // 下拉刷新，重置位置

                // 原有的数据已获得，下面请求广告，结果在 OnMJAdListener 的回调接口中
                feedAdOnly.refresh(AD_NUM_PER_PAGE);


            }
        });


        adapter = new NormalCustomAdapter();
        adapter.setList(data);

        rv_content.setLayoutManager(new LinearLayoutManager(this));
        rv_content.setAdapter(adapter);


        // 以下声明一次即可
        // 注意：必须主动调用  feedAdOnly.refresh() 之后，才会有广告回调回来。 建议开发者在获取自己的数据之后再调用 feedAdOnly.refresh()
        feedAdOnly = new FeedAdOnly(FeedListCustomActivity.this);
        MJAd.showFeedAd(this, feedAdOnly, "5", new OnMJAdListener() {
            @Override
            public void onAdLoadSuccess() {
                LogUtil.d("feed ad success");

                // 广告请求成功后，会回调这里，拿到广告数据后，插入adapter里

                List<MJFeedAdView> adsList = feedAdOnly.getFeedAdList();

                // 拿到广告后，插进显示的datalist中 , 以下仅为演示，开发者可根据自己需求插入位置，需要注意不要数组越界

                int insertPosition = lastAdInsertPosition;

                int offset = (data.size() - insertPosition) / AD_NUM_PER_PAGE;

                for (int i = 0; i < adsList.size(); i++) {

                    insertPosition = insertPosition + offset;
                    if (insertPosition < data.size()) {
                        CustomModel customModel = new CustomModel();
                        customModel.mjFeedAdView = adsList.get(i);
                        data.add(insertPosition, customModel);

                        lastAdInsertPosition = insertPosition; // 记录最新插入位置
                    }
                }

                adapter.notifyDataSetChanged();

            }

            @Override
            public void onAdLoadFail(ErrorModel errorModel) {
                LogUtil.d("feed ad fail " + errorModel);

            }

            @Override
            public void onAdClicked() {
                LogUtil.d("feed ad click");

            }

            @Override
            public void onAdDismiss() {
                LogUtil.d("feed ad dismiss");

            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public class CustomModel {

        public String text;
        public MJFeedAdView mjFeedAdView = null;  // demo 里是在原有model里添加MJFeedAdView字段，通过是否为null，来判断是是不是广告。除此外，也有许多其他方式来标识

        /**
         * @return 是否是广告
         */
        public boolean isAd() {
            return mjFeedAdView != null;
        }

    }


    /**
     * 因为既要显示正常数据，也要显示广告，二者的layout不一致，所以采用多种viewType实现。 方法不唯一
     * getItemViewType 区分正常类型 和 广告类型 两种
     */
    public class NormalCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


        public ArrayList<CustomModel> dataList;


        public static final int TYPE_DATA = 1; // 普通类型
        public static final int TYPE_AD = 2;   // 广告

        public NormalCustomAdapter() {

        }

        public void setList(ArrayList<CustomModel> list) {
            dataList = list;
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }


        @Override
        public int getItemViewType(int position) {
            if (dataList.get(position).isAd()) {
                return TYPE_AD;
            } else {
                return TYPE_DATA;
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            switch (viewType) {
                case TYPE_AD:
                    View adView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ad_item, null);
                    return new AdViewHolder(adView);

                case TYPE_DATA:
                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1, null);
                    return new VH(view);
                default:
                    return null;
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            int viewType = getItemViewType(position);
            switch (viewType) {
                case TYPE_AD:
                    //通过sdk获取到的广告view，插入到adapter里，并显示 . 记得调用 showItem 方法。

                    RelativeLayout rl = (RelativeLayout) holder.itemView;
                    rl.removeAllViews();

                    MJFeedAdView mjFeedAdView = dataList.get(position).mjFeedAdView;

                    // 移除 view
                    if (mjFeedAdView.getParent() != null) {
                        ((ViewGroup) mjFeedAdView.getParent()).removeView(mjFeedAdView);
                    }

                    rl.addView(mjFeedAdView);

                    mjFeedAdView.showItem(); // 显示广告 ，必须调用后才会显示广告

                    break;
                case TYPE_DATA:

                    VH vh = (VH) holder;
                    vh.title.setText(dataList.get(position).text);
                    break;
                default:
                    break;
            }
        }


        /**
         * 广告的 viewholder
         */
        public class AdViewHolder extends RecyclerView.ViewHolder {

            public AdViewHolder(View view) {
                super(view);
            }
        }


        public class VH extends RecyclerView.ViewHolder {
            public final TextView title;
            public ViewGroup container;

            public VH(View v) {
                super(v);
                title = v.findViewById(R.id.tv_text);
            }
        }
    }


}
