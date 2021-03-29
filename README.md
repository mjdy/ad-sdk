# MJ广告SDK－－接入说明文档 V1.7.7
## 1. SDK集成

```
// 必选
implementation 'com.mjdy.ad:base:1.7.7'

// 可选
implementation 'com.mjdy.ad:gdt:1.0.3'  // 广点通
implementation 'com.mjdy.ad:tt:1.0.8'  // 头条
implementation 'com.mjdy.ad:ks:1.0.2'  // 快手


```

## 2. 接入代码
### 2.1 初始化


在项目根目录的 **build.gradle** 添加如下maven库地址：

```
allprojects {
    repositories {
    
        ...
         
        maven { url "https://jitpack.io" }
        maven { url "https://raw.githubusercontent.com/mjdy/maven/master" }
        maven { url 'https://dl.bintray.com/7nc/mjdy' }

    }
}
```




在application的onCreate里面加入

```
 MJAd.init(this,"yourAppId","oaid","channel");
```
> yourAppId 需要替换为您的ID，非空
> 
> android 10 及以上需要 oaid ，其他版本可传空
> 
> channel 为自定义渠道，可为空




####  如果您打包App时的targetSdkVersion >= 23：请先获取到SDK要求的所有权限，然后再调用SDK的广告接口。需要动态申请的权限有 
```
1. Manifest.permission.READ_PHONE_STATE
2. Manifest.permission.WRITE_EXTERNAL_STORAGE

```


### 2.2 请求显示广告

显示广告有两种方式

1. app传入容器，由sdk控制，加载完即显示
2. app获取广告，app自行控制显示时机，俗称 预加载

> 两种加载方式，通过 **container** 这个参数控制。 app传入 container，即sdk控制。app不传 container，即预加载，app控制

### 2.2.1 sdk控制显示


```
                MJAdConfig adConfig = new MJAdConfig.Builder()
                        .activity(activity)
                        .container(container_view)
                        .posId("adPosId")
                        .build();

                MJAd.showAd(adConfig, new MJAdListener() {
                    @Override
                    public void onAdLoadSuccess(List<MJAdView> adViewList) {
							// 无需处理，sdk加载完广告即可显示
         
                    }
                });
```

### 2.2.2 预加载，app控制显示

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

### 2.2.2.1 跨页面预加载
有些场景，会对广告显示的速度有极高的要求，比如新开一个activity,刚进来就要显示广告。即便用了以上的预加载方式，也需要有一个加载过程，无法满足需求。所以提供了 跨页面预加载，即可以在A页面加载广告，在B页面直接展示。但这种方式会有资源浪费的情况，且对性能会有影响，切勿滥用。跨页面预加载的位置，建议不超过6个


```
     String[] posIds = new String[]{"posId1","posId2"};
     MJAdConfig config = new MJAdConfig.Builder().activity(this).posId(posIds).build();
     MJAd.preLoad(config);
```

> 建议在MainActivity中调用；
> activity 参数必须为 activity；
> posId 可为数组；
> MJAd.preLoad 只调用一次即可；
> 声明完即可，后面使用方式和预加载一样

### MJAdConfig

   字段  | 说明 | 是否必须 | 备注
---| --- | --- | ---
activity | activity | 是| 最好是activity
posId |  广告位代码 | 是 | 
container | 容器 | 否 | ViewGroup  
width | 宽度 | 否 |  单位dp，默认屏幕宽度dp
adCount | 请求广告数量 | 否| 默认 1
timeout | 请求广告超时 | 否 | 单位毫秒，默认 3000
refreshTime | 广告刷新时间 | 否 | 



## 3.其他
### 3.1 混淆
SDK已经处理，无需额外操作
### 3.2 Sample
本工程为sample工程，可作为集成参考

### 3.3 错误码

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

#### 相应平台错误码
请参见具体平台的文档。如 广点通sdk文档 or 百度sdk文档


# 更改记录
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
