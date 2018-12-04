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
import com.mobjump.mjadsdk.view.FeedAdAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cx on 11/16/18.
 */
public class NewsListActivity extends Activity {

    public static void launch(Context context) {
        context.startActivity(new Intent(context, NewsListActivity.class));
    }

    RecyclerView rv_content;


    ArrayList<String> data = new ArrayList<>();
    NormalAdapter adapter;

    FeedAdAdapter feedAdAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.news_list_activity);
        rv_content = findViewById(R.id.rv_content);

        findViewById(R.id.btn_load_more).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 20; i++) {
                    data.add("load more " + i);
                }
                // when list changed
                feedAdAdapter.refresh();
            }
        });

        findViewById(R.id.btn_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data.clear();
                for (int i = 0; i < 20; i++) {
                    data.add("refresh " + i);

                }
                // when list changed
                feedAdAdapter.refresh();
            }
        });

        for (int i = 0; i < 20; i++) {
            data.add("text " + i);
        }

        adapter = new NormalAdapter(data);
        rv_content.setLayoutManager(new LinearLayoutManager(this));

        // init feedAdAdapter
        feedAdAdapter = new FeedAdAdapter(adapter);
        // set wrapper adapter
        rv_content.setAdapter(feedAdAdapter.wrapper());

        // show feed ad
        MJAd.showFeedAd(this, feedAdAdapter, "5", new OnMJAdListener() {
            @Override
            public void onAdLoadSuccess() {

            }

            @Override
            public void onAdLoadFail(String fail) {

            }

            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdDismiss() {

            }
        });
    }


    @Override
    protected void onDestroy() {
        feedAdAdapter.destroy();
        super.onDestroy();
    }

    public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH> implements FeedAdAdapter.IGetDataList {


        public ArrayList<String> dataList;

        @Override
        public List getList() {
            // must implements  FeedAdAdapter.IGetDataList and return list
            return dataList;
        }


        public NormalAdapter(ArrayList<String> data) {
            this.dataList = data;
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
