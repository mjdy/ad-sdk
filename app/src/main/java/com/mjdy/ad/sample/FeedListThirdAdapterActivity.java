package com.mjdy.ad.sample;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.mobjump.mjadsdk.MJAd;
import com.mobjump.mjadsdk.adline.interfaces.OnMJAdListener;
import com.mobjump.mjadsdk.bean.ErrorModel;
import com.mobjump.mjadsdk.view.FeedAdAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 11/16/18.
 * 信息流广告演示demo
 * 演示第三方adapter , 以BRVAH为例
 */
public class FeedListThirdAdapterActivity extends Activity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, FeedListThirdAdapterActivity.class));
    }

    RecyclerView rv_content;  // 目前仅支持 RecyclerView

    ArrayList<String> dataList = new ArrayList<>();  // 需要显示的数据

    FeedAdAdapter feedAdAdapter;  // 广告sdk封装的adapter

    NormalBrvahAdapter brvahAdater;


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

                dataList.addAll(newList);

//              brvahAdater.addData(newList);  // 这个也可以，只是会浪费点性能。addData 里会刷新adapter，在feedAdAdapter.refresh() 里仍会刷新一次

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
                dataList.clear();
                dataList.addAll(newList);

                // 如果是下面这种方式给data赋值 , 需要调用 feedAdAdapter.setDataList();
//                dataList = newList;
//                brvahAdater.setNewData(dataList);  // 会浪费点性能，setNewData 里会刷新adapter，在feedAdAdapter.refresh() 里仍会刷新一次
//                feedAdAdapter.setDataList(brvahAdater.getData());

                // when list changed 数据更改时要调用此方法刷新数据
                feedAdAdapter.refresh();

            }
        });

        for (int i = 0; i < 20; i++) {
            dataList.add("text " + i);
        }

        brvahAdater = new NormalBrvahAdapter(dataList);
        brvahAdater.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                // 因为feedAdAdapter对数据进行了处理，获取position时需要通过  feedAdAdapter.getRealPosition(position) 得到原有的真正位置

                int realPosition = feedAdAdapter.getRealPosition(position);
                LogUtil.d("position is " + realPosition + "  data is " + dataList.get(realPosition));
                Toast.makeText(FeedListThirdAdapterActivity.this, "position is " + realPosition + "  data is " + dataList.get(realPosition), Toast.LENGTH_SHORT).show();
            }
        });
        rv_content.setLayoutManager(new LinearLayoutManager(this));

        // init feedAdAdapter
        feedAdAdapter = new FeedAdAdapter(brvahAdater);
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


    public class NormalBrvahAdapter extends BaseQuickAdapter<String, BaseViewHolder> implements FeedAdAdapter.IGetDataList {

        public NormalBrvahAdapter(@Nullable List<String> dataList) {
            super(R.layout.item_1, dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.tv_text, item);
        }

        @Override
        public List getList() {
            return getData();
        }
    }


}
