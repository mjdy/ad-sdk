# MJ广告SDK－－接入说明文档 V2.0.7
## 1. SDK集成


在项目根目录的 **build.gradle** 添加如下maven库地址：

```
allprojects {
    repositories {
    
        ...

        maven { url "https://jitpack.io" }
        maven { url 'http://maven.7nc.top/repository/mjdy/' }


    }
}
```


在app层 **build.gradle** 里添加如下依赖：

```
dependencies {
		
		...
		
		implementation 'com.mjdy.ad:sdk:2.0.7'

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
			
		MJAd.showAd(adConfig, new MJAdListener() {
			@Override
			public void onAdLoadSuccess(List<MJAdView> adViewList) {
			    
					mjAdView = adViewList.get(0); // 拿到广告view
					
					mjAdView.show(container_view); // 在需要的时机调用 show  即可显示
			 
			}
		});
			    
			    
		

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

### MJAdConfig

   字段  | 说明 | 是否必须 | 备注
---| --- | --- | ---
activity | activity | 是| 最好是activity
posId |  广告位代码 | 是 | 
width | 宽度 | 否 |  单位dp，默认屏幕宽度dp
adCount | 请求广告数量 | 否| 默认 1
timeout | 请求广告超时 | 否 | 单位毫秒，默认 3000
refreshTime | 广告刷新时间 | 否 | 



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


#### 相应平台错误码
请参见具体平台的文档。如 广点通sdk文档 or 百度sdk文档


# 更改记录

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
