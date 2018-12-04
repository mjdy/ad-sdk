# MJ广告SDk－－接入说明文档 V1.0
## 1. SDK集成

```
// 必选
implementation 'com.mjdy.ad:base:1.0.0' 

// 可选
implementation 'com.mjdy.ad:bd:1.0.0'   // 百度
implementation 'com.mjdy.ad:gdt:1.0.0'  // 广点通

```

## 2. 接入代码
### 2.1 初始化
在application的onCreate里面加入

```
MJAd.init("this","yourAppId");
```
> yourAppId 需要替换为您的ID

####  如果您打包App时的targetSdkVersion >= 23：请先获取到SDK要求的所有权限，然后再调用SDK的广告接口。需要动态申请的权限有 
```
1. Manifest.permission.READ_PHONE_STATE
2. Manifest.permission.WRITE_EXTERNAL_STORAGE

```


### 2.2 开屏广告
```
            MJAd.showSplashAd(activity, "posAdId", new OnHTAdListener() {
                    @Override
                    public void onAdLoadSuccess()
                    }

                    @Override
                    public void onAdLoadFail(int failCode) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
```
### 2.3 插屏广告
```
          MJAd.showInterstitialAd(activity, "posAdId", new OnHTAdListener() {
                    @Override
                    public void onAdLoadSuccess() {
                        
                    }

                    @Override
                    public void onAdLoadFail(int i) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
```
### 2.4 Banner广告
在需要引入的layout里添加

```
    <com.mobjump.mjadsdk.view.MJBannerView
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"></com.mobjump.mjadsdk.view.MJBannerView>
```

在代码里

```
		 MJBannerView banner_view = findViewById(R.id.banner_view);
		  
         MJAd.showBannerAd(activity, banner_view, "podAdId", new OnHTAdListener() {
                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(int failCode) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
```

在activity销毁时，建议调用view销毁

```
    @Override
    protected void onDestroy() {
        banner_view.destroy();
        super.onDestroy();
    }
```


### 2.5 信息流广告

以RecyclerView为例，添加信息流广告即是在adapter中新增一种展示类型，为了方便开发者集成，尽量对原有adapter不作修改，sdk进行了二次封装，参考流程如下：

#### 1. 原有adapter实现FeedAdAdapter.IGetDataList接口
```
    public class NormalAdapter extends RecyclerView.Adapter implements FeedAdAdapter.IGetDataList {
        public List dataList; // 实现List接口的数据结构均可，如ArrayList

        @Override
        public List getList() {
            return dataList;
        }
        
        @Override
        public int getItemCount() {
            return dataList.size();
        }
        
        ...
```
getList() 需返回adapter显示的内容数据，否则无法显示广告

#### 2.对原有adapter进行包装

```
        RecyclerView rv_content = findViewById(R.id.rv_content);
        ...
       
        FeedAdAdapter feedAdAdapter = new FeedAdAdapter(originAdapter); // originAdapter需实现 FeedAdAdapter.IGetDataList
        rv_content.setAdapter(feedAdAdapter.wrapper());
```

originAdpater即为原有的adapter，如第一步中的NormalAdapter，需注意，原有adapter必须实现FeedAdAdapter.IGetDataList。然后将 feedAdAdapter.wrapper() 包装后的adapter作为参数，传递给RecyclerView的setAdapter

#### 3. 设置信息流广告ID
设置ID应该在RecyclerView setAdater 之后

```
        MJAd.showFeedAd(activity, feedAdAdapter, "posAdId", new OnMJAdListener() {
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
```

#### 4. 数据变更时的adapter刷新
如果信息流内容有变化，比如加载更多，需要调用FeedAdAdapter的刷新

```
feedAdAdapter.refresh();
```
#### 5. 数据销毁
如果界面销毁，需要调用FeedAdAdapter的destroy,比如在onDestroy里调用

```
    @Override
    protected void onDestroy() {
        feedAdAdapter.destroy();
        super.onDestroy();
    }
```


> 上述广告形式，都需要独立的 posAdId
> 上述广告形式，OnMJAdListener 接口为回调监听，如无需要，可传null

### 2.6 浮窗广告
TO DO


## 3.其他
### 3.1 混淆
SDK已经处理，无需额外操作
### 3.2 Sample
本工程为sample工程，可作为集成参考
