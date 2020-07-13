# MJ广告SDK－－接入说明文档 V1.1.8
## 1. SDK集成

```
// 必选
implementation 'com.mjdy.ad:base:1.1.6'

// 可选
implementation 'com.mjdy.ad:bd:1.0.2'   // 百度
implementation 'com.mjdy.ad:gdt:1.0.1'  // 广点通
implementation 'com.mjdy.ad:tt:1.0.4'  // 头条


```

## 2. 接入代码
### 2.1 初始化


在项目根目录的 **build.gradle** 添加如下两条maven库地址：

```
allprojects {
    repositories {
    
        ...
         
        maven { url "https://jitpack.io" }
        maven { url "https://raw.githubusercontent.com/mjdy/maven/master" }

    }
}
```




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

#### 信息流广告集成有两种方式。一种是用sdk提供的封装adapter，另外一种是sdk提供adView，开发者自行插入

### 2.5.1 信息流广告封装adapter集成（推荐）


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

#### 注意事项

> 1. 如果传入给adapter的dataList地址发生改变，比如下拉刷新时，从网络请求到的数据，直接写成 dataList = dataListFromNet ，导致原有dataList地址发生改变，此时需要调用 feedAdAdapter.setDataList() 更新数据 ,sample里有相关说明



> 2. 以上是原生RecyclerView.Adapter的集成方式，如果开发者用了第三方的封装adapter，比如brvah，对position进行了封装，如果开发者需要获取原有数据的位置，需要调用 feedAdAdapter.getRealPosition(position) 来取得。sample里提供了BRVAH集成的demo，详见 **FeedListThirdAdapterActivity.class**


### 2.5.2 信息流广告adview插入集成
当封装adapter无法满足开发者需求时，可以选用获取adView插入即成的方式

集成方式与封装adapter类似，将 FeedAdAdapter 替换为 FeedAdOnly，传给MJAd . 

调用 feedAdOnly.refresh() 请求广告。参数可传入int，为请求几条广告，默认为3

将请求成功的 List<MJFeedAdView> 插入到开发者自己的数据结构中，调用  **MJFeedAdView.showItem()** 显示

完整实现可参照 **FeedListCustomActivity.class**


```
        FeedAdOnly feedAdOnly = new FeedAdOnly(activity);
        
        MJAd.showFeedAd(activity, feedAdOnly, "posAdId", new OnMJAdListener() {
            @Override
            public void onAdLoadSuccess() {

					// 获取广告成功后，会回调这里
			       List<MJFeedAdView> adsList = feedAdOnly.getFeedAdList();
			       
			       // 插入到adapter里 ...
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
        
        feedAdOnly.refresh(3); // 必须调用该方法，才会请求广告。
```



> 与封装adapter不同，广告adView必须主动调用refresh后，才会请求广告


### 2.6 激励视频

激励视频与其他不同的是，回调接口是 **OnMJRewadVideoListener** ，多了两个方法

1. onVideoPlayFinish() 视频播放完成
2. onReward(RewardModel rewardModel)  奖励的回调


RewardModel结构如下：
 
 字段名 | 类型 | 说明
 --- | --- | ---
 name | string | 奖励名称
 amount | int | 奖励数量


```
 MJAd.showVideoRewardAd(activity, "rewardVideoPosId", new OnMJRewadVideoListener() {


                    @Override
                    public void onVideoPlayFinish() {

                    }

                    @Override
                    public void onReward(RewardModel rewardModel) {

                    }

                    @Override
                    public void onAdLoadSuccess() {

                    }

                    @Override
                    public void onAdLoadFail(ErrorModel errorModel) {

                    }

                    @Override
                    public void onAdClicked() {

                    }

                    @Override
                    public void onAdDismiss() {

                    }
                });
```

## 3.其他
### 3.1 混淆
SDK已经处理，无需额外操作
### 3.2 Sample
本工程为sample工程，可作为集成参考

### 3.3 已知问题
1. sample里的百度的开屏广告请求失败，更换为正式id后应该正常
2. 第一次运行sample ，需要获取百度or广点通的相关数据，在数据获取成功前，请求广告会失败。网络正常的情况下，一般5秒后即可

### 3.4 错误码

**onAdLoadFail** 方法里提供了 ```ErrorModel``` 参数。

```
	@Override
        public void onAdLoadFail(ErrorModel errorModel) {
                errorModel.platform;    // 广告平台
	        errorModel.code;        // 错误码
	        errorModel.message;     // 错误信息
        }
```




#### 通用错误码

sdk提供了 **ErrorModel** 类 , 错误码常量可通过  ```ErrorModel.ERROR_LOADING ``` 获取

   ID   | 错误码 | 错误说明
---| --- | ---
ERROR_LOADING | 1001 |  sdk正在加载中，需等待sdk加载完毕
ERROR\_LOADED_FAIL | 1002 | sdk加载失败，建议重启app
ERROR\_POSID_NULL | 1003 | 广告位id未空
ERROR_PERMISSION | 1004 | 没有给予PHONE or STORAGE  权限
ERROR\_POSID_ERROR | 1005 |  广告位id错误


#### 平台码
sdk提供了 **ErrorModel** 类 , 平台码常量可通过  ```ErrorModel.PLATFORM_GDT ``` 获取

   ID   | 平台码 | 平台说明
---| --- | ---
PLATFORM_UNKOWN | -1 |  用于显示通用错误
PLATFORM_GDT | 1 | 广点通
PLATFORM_BD | 2 | 百度
PLATFORM_QSZ | 3 | 启示者
PLATFORM_TT | 4 | 头条

#### 相应平台错误码
请参见具体平台的文档。如 广点通sdk文档 or 百度sdk文档


# 更改记录

## 1.1.8
1. 整合了新的实现方式
2. 增加了新平台

## 1.1.6
1. 广点通banner和插屏升级为2.0

## 1.1.5
1. 修复配置文件解析异常

## 1.1.4
1. 修复头条的混淆问题

## 1.1.3
1. 修复头条可能遇到的问题

## 1.1.2
1. 新增激励视频

## 1.1.1
1. 完善了信息流的集成，支持第三方adapter
2. 信息流广告提供view的形式，开发者可自行获取插入

## 1.1.0
1. banner广告获取失败时，不显示banner
2. 修复了信息流广告初始化为空时 导致的数据不刷新bug

## 1.0.9
1. 修改了配置地址

## 1.0.8
1. 完善了上报机制

## 1.0.7
1. 更改onAdLoadFail参数，增加广告平台，错误码，错误信息字段

## 1.0.6
1. 如果未赋予权限，会调用onFail，打印log

## 1.0.5
1. 完善了上报机制
2. 修复了部分广告不显示的bug
3. add log

## 1.0.4
1. 修复banner广告文字提示重复的bug

## 1.0.3
1. 修复了初始化延迟导致banner不显示的bug
2. 修复了由于权限问题导致信息流崩溃的bug
3. banner加入广告文字提示
4. 添加里中文注释

## 1.0.2
1. 修复了加载异常的bug

## 1.0.0
1. sdk发布，提供了 开屏，插屏，banner，信息流的广告
