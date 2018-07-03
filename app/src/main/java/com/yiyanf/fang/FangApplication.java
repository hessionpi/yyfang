package com.yiyanf.fang;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.support.multidex.MultiDex;

import com.huawei.android.pushagent.PushManager;
import com.tencent.TIMGroupReceiveMessageOpt;
import com.tencent.TIMManager;
import com.tencent.TIMOfflinePushListener;
import com.tencent.TIMOfflinePushNotification;
import com.tencent.cos.common.COSEndPoint;
import com.tencent.qalsdk.sdk.MsfSdkUtils;
import com.tencent.qcloud.presentation.event.MessageEvent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.yiyanf.fang.api.ApiManager;
import com.yiyanf.fang.im.PushUtil;
import com.yiyanf.fang.im.TCIMInitMgr;
import com.yiyanf.fang.util.LogUtil;

import java.util.Locale;

import cn.jpush.android.api.JPushInterface;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Hition on 2017/9/19.
 */

public class FangApplication extends Application {

    private static FangApplication instance;

    {
        Config.DEBUG = false;
        PlatformConfig.setWeixin("wx3b6694f82a3c8cc7", "fa25b3bb5a84c93f71d166054a1cb030");
    }

    public static FangApplication getApplication() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        UMConfigure.init(getApplicationContext(),UMConfigure.DEVICE_TYPE_PHONE,null);
        //包含fragment的程序中 禁止默认的页面统计方式，这样将不会再自动统计Activity
        MobclickAgent.openActivityDurationTrack(false);
        /**
         * 设置网络请求的环境
         */
        checkEnvironment();

        initIMSDK();
        initUMShare();
        initPush();
        initRealm();

        // 注册离线推送监听器
        if(MsfSdkUtils.isMainProcess(this)) {
            TIMManager.getInstance().setOfflinePushListener(new TIMOfflinePushListener() {
                @Override
                public void handleNotification(TIMOfflinePushNotification notification) {
                    if (notification.getGroupReceiveMsgOpt() == TIMGroupReceiveMessageOpt.ReceiveAndNotify){
                        //消息被设置为需要提醒
                        notification.doNotify(getApplication(), R.mipmap.ic_launcher);
                    }
                }
            });
        }
    }

    // 检测环境，然后根据环境设置开发环境和线上环境
    private void checkEnvironment() {
        boolean isDubugPackage = isApkInDebug();
        if(isDubugPackage){ // develop 版本配置
            makeDevelopConfig();
        }else{ // release 版本
            makeOnlineConfig();
        }
    }

    /**
     * 线上环境配置
     */
    private void makeOnlineConfig() {
        // 配置API访问地址
        ApiManager.mIsDevelopEnv = false;
        ApiManager.setDevEnv();
        // 关闭Log输出
        LogUtil.isDebug = false;
        // 极光推送，发布时关闭日志
        JPushInterface.setDebugMode(false);

        // 云通信服务相关配置
        FangConstants.IMSDK_APPID = 1400079609;
        // COS存储服务相关配置
        FangConstants.COS_BUCKET = "fang";
        FangConstants.COS_APPID = "1256351189";
        FangConstants.COS_REGION = COSEndPoint.COS_SH;
    }

    /**
     * 开发环境配置
     */
    private void makeDevelopConfig() {
        // 配置API访问地址
        ApiManager.mIsDevelopEnv = true;
        ApiManager.setDevEnv();
        // 开启Log输出
        LogUtil.isDebug = true;
        // 极光推送，设置开启日志
        JPushInterface.setDebugMode(true);
        // 云通信服务相关配置
        FangConstants.IMSDK_APPID = 1400046151;
        // COS存储服务相关配置
        FangConstants.COS_BUCKET = "fang";
        FangConstants.COS_APPID = "1254235267";
        FangConstants.COS_REGION = COSEndPoint.COS_SH;
    }

    /**
     * 是否是debug版
     *
     * @return true 是debug版 else release版
     */
    private boolean isApkInDebug(){
        try {
            ApplicationInfo info = getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 初始化推送
     */
    private void initPush() {
        // 初始化极光推送 JPush
        JPushInterface.init(this);

        //初始化程序后台后消息推送
        PushUtil.getInstance();
        //初始化消息监听
        MessageEvent.getInstance();
        String vendor = Build.MANUFACTURER;
        if(vendor.toLowerCase(Locale.ENGLISH).contains("xiaomi")) {
            //注册小米推送服务
            MiPushClient.registerPush(this, "2882303761517702567", "5601770296567");
        }else if(vendor.toLowerCase(Locale.ENGLISH).contains("huawei")) {
            //请求华为推送设备token
            PushManager.requestToken(this);
        }
    }

    /**
     * 初始化Realm 数据库操作
     */
    private void initRealm(){
        Realm.init(this);
        RealmConfiguration configuration=new RealmConfiguration.Builder()
                .name(FangConstants.DB_NAME)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(configuration);
    }

    /**
     * 初始化友盟分享
     */
    private void initUMShare() {
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(getApplicationContext()).setShareConfig(config);
    }

    public void initIMSDK() {
        TCIMInitMgr.init(getApplicationContext());
    }

    /**
     * 主要解决dex突破65535时候，项目在android5.0 以下报错Java.lang.NoClassDefFoundError
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }



}
