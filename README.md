# MJ广告SDK－－接入说明文档 V2.2.3
## 1. SDK集成


在项目根目录的 **build.gradle** 添加如下maven库地址：

```
allprojects {
    repositories {
    
        ...

        maven { url "https://jitpack.io" }
        maven { url 'http://maven.7nc.top/repository/mjdy/' }
        maven { url "https://artifact.bytedance.com/repository/pangle" }

    }
}
```


在app层 **build.gradle** 里添加如下依赖：

```
dependencies {
		
		...
		
		implementation 'com.mjdy.ad:sdk:2.2.3'

}
```


## 2. 接入代码
### 2.1 初始化






在application的onCreate里面加入

```
 MJAd.init(this,"yourAppId","yourChannel");
```
> yourAppId 需要替换为您的ID，非空
> 
> yourChannel 为自定义渠道，可为空




####  如果您打包App时的targetSdkVersion >= 23：请先获取到SDK要求的所有权限，然后再调用SDK的广告接口。需要动态申请的权限有 
```
1. Manifest.permission.READ_PHONE_STATE
2. Manifest.permission.WRITE_EXTERNAL_STORAGE

```


### 2.2 请求显示广告

### 2.2.1 配置config后，获取mjadView，在适当的时机，调用show


```
		
		MJAdView mjAdView;
		   
		MJAdConfig adConfig = new MJAdConfig.Builder()
		    .activity(activity)
		    .posId("adPosId")
		    .build();
			
		MJAd.loadAd(adConfig, new MJAdListener() {
			@Override
			public void onAdLoadSuccess(List<MJAdView> adViewList) {
			    
					mjAdView = adViewList.get(0); // 拿到广告view
					
					mjAdView.show(container_view); // 在需要的时机调用 show  即可显示
			 
			}
		});
			    
			    
		

```

**MJAdListener** 为回调接口 ， 默认只强制回调 onAdLoadSuccess , 其他按需加载

```

    /**
     * 广告加载成功的回调
     * <p>
     * adViewList参数极大概率有值，即便如此，也强烈建议进行非空判断
     * <p>
     *
     * @param adViewList
     */
    public abstract void onAdLoadSuccess(List<MJAdView> adViewList);


    /**
     * 广告失败
     *
     * @param errorModel
     */
    public void onAdLoadFail(ErrorModel errorModel) {
    }


    /**
     * 广告展示
     */
    public void onAdShow() {

    }

    /**
     * 广告点击
     */
    public void onAdClicked() {
    }

    /**
     * 广告消失的回调。倒计时结束，用户关闭 等等。
     * <p>
     * 大多数情况可忽略参数mjAdView。
     * mjAdView是用于信息流，用户点击关闭后，app端的信息流dataList应该移除这一条
     *
     * @param mjAdView
     */
    public void onAdDismiss(MJAdView mjAdView) {
    }

    /**
     * 视频播放完成
     */
    public void onAdVideoPlayFinish() {
    }


    /**
     * 视频广告已缓存
     */
    public void onAdVideoCached() {

    }


    /**
     * 开屏，视频等跳过,部分平台没有该回调。不保证100%有效
     * 不要被方法名迷惑，所有跳过均在此回调，包括开屏
     */
    public void onAdVideoSkip() {
    }


    /**
     * 奖励视频的回调
     */
    public void onAdReward(boolean rewardVerify, int rewardAmount, String rewardName) {

    }

    /**
     * 自渲染view
     *
     * @param view
     */
    public void onAdCustomView(View view) {

    }
```


### 2.2.2 sdk自动预加载
sdk自动为指定的posId缓存广告，sdk持有一个广告池的概念，池中始终持有一个广告实例。在app层索要广告时，将池中的广告给app层，同时加载一个新的广告，放入池中。这样会提高app层获取广告的效率，但会有资源浪费的情况，且对性能会有影响，切勿滥用，自动预加载的位置，建议不超过6个


```
     String[] posIds = new String[]{"posId1","posId2"};
     MJAdConfig config = new MJAdConfig.Builder().activity(this).posId(posIds).build();
     MJAd.preLoad(config);
```

1. preLoad 中的activity一定要常驻，建议在MainActivity中调用；
2. activity 参数必须为 activity；
3. posId 可为数组；
4. MJAd.preLoad 只调用一次即可；
5. 可通过mjAdView.isPreLoad()来获得此次加载，是否为自动预加载

### 2.2.3 mjAdView
- 通过 **mjAdView.getPrice()** 可以获得当前广告的价格
- 通过 **mjAdView.getProfit()** 可以获得当前广告的分成比例 0-100
- 通过 **mjAdView.isValid()** 获得当前view是否可用，若返回false ， 请销毁该view，重新请求
- 通过 **mjAdView.getInfo()** 获得当前广告实例的详细信息 ，仅用于调试

mjadView.show() 有多种形式

```
show(ViewGroup viewGroup) // 默认，tag为加载config时的activityName
show(String tag, ViewGroup viewGroup) // 传入指定tag，用于标记
```

### MJAdConfig

   字段  | 说明 | 是否必须 | 备注
---| --- | --- | ---
activity | activity | 是| 最好是activity
posId |  广告位代码 | 是 |  插槽ID
width | 宽度 | 否 |  指定广告宽度，单位dp，默认屏幕宽度dp
adCount | 请求广告数量 | 否| 默认 1
isSdkContainer | 是否使用sdk的容器 | 否 | 多用于开屏。true的时候，由sdk弹出activity展示开屏
layout | 自渲染模式下的自定义view | 否 | R.layout.xxx 。 xml里的id必须按规则来，详细规则联系相关人员

### 内置配置文件
由于sdk初始化需要时间，app首次安装启动，可能会因为sdk未初始化完，而导致首次开屏广告加载失败。

所以提供内置配置文件功能，只要将首次启动需要展示的的**posId**放在内置配置文件里，即可提高app首次启动 广告展示成功率

内置配置文件命名为 **mj\_ad\_config.json** ，放在 **app/src/main/assets** 目录下，文件内容由相关人员提供


```
示例 

{"data":{"params":"abcdefg"},"result":1,"ts":1614165396066}
```

### 更多功能

以下功能为非必需功能，有需要则使用。无需求则忽略

### 1. 获取买量信息

- **MJAD.isHavePlan()** 是否为买量用户。true = 是
- **MJAD.getAccountId()** 获取买量账户id，可能为空
- **MJAD.getPlanId()** 获取买量计划id，可能为空

### 2. 获取preLoad状态
- **MJAD.isPreLoadOK()** true = preLoad成功，sdk 广告池里有数据

### 3. 获取oaid
- **MJAD.getOaid()**  获取oaid，可能为空

### 4. 初始化及激活状态

- **MJAD.setInitListener(MJInitListener mjInitListener)**  设置初始化成功监听。若初始化失败，则无此回调。注意，该回调可能会返回多次（每次启动 以及 各种意外情况自重启时）
- **MJAD.setActiveListener(MJActiveListener mjActiveListener)**  设置激活监听。激活成功，有此回调，有且仅有一次

### 5. 获取新安装apk的状态
- **MJAD.isApkSourceOk(String pkgName)**  一般用于app层监听apk安装事件，在得到系统回调时，用此方法判断该app的来源，true = 该app是通过点击sdk的广告下载安装的

## 3.其他
### 3.1 混淆
SDK已经处理，无需额外操作
### 3.2 Sample
本工程为sample工程，可作为集成参考
### 3.3 集成报错
#### 3.3.1 AAPT: error: unexpected element \<queries\> found in <manifest>
该错误出现原因为头条sdk使用了android 11的字段，导致旧版本不支持。解决方案为：更改根目录下 build.gradle里的 **classpath 'com.android.tools.build:gradle** 版本号，改为如下版本中任一即可

- 3.3.3
- 3.4.3
- 3.5.4
- 3.6.4
- 4.0.1

### 3.4 sdk大小优化

本sdk默认集成了 头条，快手，广点通，百度，游可赢 5家广告平台。如果明确不需要某一家平台，可以exclude出去，以减少apk大小，如无需求，保持默认即可

 以下仅为示例，切勿全部exclude，否则无法显示广告

```
    implementation ('com.mjdy.ad:sdk:version'){
        exclude group: 'com.mjdy.ad', module: 'gdt' // 广点通
        exclude group: 'com.mjdy.ad', module: 'tt'  // 头条
        exclude group: 'com.mjdy.ad', module: 'bd'  // 百度
        exclude group: 'com.mjdy.ad', module: 'ks'  // 快手
        exclude group: 'com.mjdy.ad', module: 'klevin'  // 游可赢
        exclude group: 'com.mjdy.ad', module: 'mb'  // GroMore
        exclude group: 'com.mjdy.ad', module: 'jd'  // 京东

    }
```


### 3.5 错误码

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
ERROR\_POSID_NULL | 1003 | 广告位id为空
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
PLATFORM_KS | 6 | 快手
PLATFORM_KLEVIN | 9 | 游可赢
PLATFORM_MB | 15 | GroMore
PLATFORM_JD | 16 | 京东




#### 相应平台错误码
请参见具体平台的文档。如 广点通sdk文档 or 百度sdk文档


# 更改记录

## 2.2.3
1. 修复缓存问题

## 2.1.9
1. 新增京东平台
2. 修复GroMore激励视频回调问题

## 2.1.3
1. 新增groMore平台
2. 去掉头条平台

## 2.0.9
1. 开屏改为弹出activity
2. 完善文档

## 2.0.7
1. 快手修复开屏问题
2. 快手支持插屏
3. 游可赢支持  模板渲染 开屏，插屏

## 2.0.4
1. 新增游可赢平台

## 1.9.6
1. 整合了sdk，去掉sdk控制加载的模式

## 1.9.0
1. 优化了代码位的分配


## 1.8.9
1. 替换jcenter源

## 1.8.7
1. 新增百度平台，支持 开屏 和 激励视频
2. 修复激励视频可能引起的内存溢出 

## 1.8.6
1. 新增快手激励视频模板渲染
2. 新增 mjAdView.getPrice()

## 1.8.5
1. 修复miui获取oaid的问题

## 1.8.2
1. sdk集成oaid，无需app层传入

## 1.7.7
1. 修复了配置文件为空导致的加载失败
2. 缩短了激活时间
3. 修复了激励视频显示问题

## 1.6.6
1. 替换为反屏蔽包


## 1.3.7
1. 新增了快手平台


## 1.2.6
1. 强化了预加载的逻辑


## 1.1.9
1. 去除广告位类型，统一为showAd

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
