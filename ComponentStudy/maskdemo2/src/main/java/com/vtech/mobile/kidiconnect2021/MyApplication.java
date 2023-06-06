package com.vtech.mobile.kidiconnect2021;

import android.app.Application;
import android.content.Context;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;

public class MyApplication extends Application {

    private String TAG = "MyApplication";

    public static final String OFFLINE_AUTH_KEY = "";

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = this;
//        FileManagerUtil.initFilePath(this);
//        updateLocalEffect();

        // 离线鉴权
        TiSDK.initOffline(OFFLINE_AUTH_KEY, getApplicationContext());

        // 关闭日志输出
        TiSDK.setLog(true);

        // 关闭美颜 API在Ti的官方文档查阅
        TiSDKManager.getInstance().setBeautyEnable(false);
        // 关闭瘦脸
        TiSDKManager.getInstance().setFaceTrimEnable(false);

    }


    public static Context getContext() {
        return mContext;
    }

}
