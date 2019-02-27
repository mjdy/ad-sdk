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
import android.widget.TextView;

import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.view.FeedAdAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 11/16/18.
 * 信息流广告演示demo
 */
public class FeedListActivity extends Activity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, FeedListActivity.class));
    }

    RecyclerView rv_content;  // 目前仅支持 RecyclerView

    ArrayList<String> data = new ArrayList<>();  // 需要显示的数据

    NormalAdapter adapter;  // 开发者自己的adapter

    FeedAdAdapter feedAdAdapter;  // 广告sdk封装的adapter


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_list_activity);
        rv_content = findViewById(R.id.rv_content);

        findViewById(R.id.btn_load_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加载更多

                ArrayList<String> newList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    newList.add("load more  " + i);
                }

                data.addAll(newList);

                // when list changed 数据更改时要调用此方法刷新数据
                feedAdAdapter.refresh();
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // 模拟下拉刷新

                ArrayList<String> newList = new ArrayList<>();
                for (int i = 0; i < 20; i++) {
                    newList.add("refresh  " + i);
                }

                // 这种方式用的是使用原有的data
                data.clear();
                data.addAll(newList);

                // 如果是下面这种方式给data赋值，需要更新adapter的list，同时要调用  feedAdAdapter.setDataList(data);
//                data = newList;
//                adapter.setDataList(data);
//                feedAdAdapter.setDataList(data);

                // when list changed 数据更改时要调用此方法刷新数据
                feedAdAdapter.refresh();
            }
        });


        adapter = new NormalAdapter();

        // 给adapter一个需要显示的数据结构
        adapter.setDataList(data);

        rv_content.setLayoutManager(new LinearLayoutManager(this));

        // init feedAdAdapter
        feedAdAdapter = new FeedAdAdapter(adapter);
        // set wrapper adapter
        rv_content.setAdapter(feedAdAdapter.wrapper());

        // show feed ad
        MJAd.showFeedAd(this, feedAdAdapter, "5", new OnMJAdListener() {
            @Override
            public void onAdLoadSuccess() {
                LogUtil.d("feed ad success");

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
        feedAdAdapter.destroy(); // 为了更好的回收内存，建议在界面销毁时调用此方法
        super.onDestroy();
    }


    /**
     * 封装adapter共2步
     * 1. 实现 FeedAdAdapter.IGetDataList
     * 2. getList 中返回原始数据列表
     */
    public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH> implements FeedAdAdapter.IGetDataList {


        public ArrayList<String> dataList;


        @Override
        public List getList() {
            // must implements  FeedAdAdapter.IGetDataList and return list
            return dataList;
        }

        public void setDataList(ArrayList<String> dataList) {
            this.dataList = dataList;
        }


        public NormalAdapter() {
        }

        @Override
        public void onBindViewHolder(VH holder, int position) {
            holder.title.setText(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_1, null);
            return new VH(view);
        }

        public class VH extends RecyclerView.ViewHolder {
            public final TextView title;
            public ViewGroup container;

            public VH(View v) {
                super(v);
                title = (TextView) v.findViewById(R.id.tv_text);
            }
        }
    }


}
