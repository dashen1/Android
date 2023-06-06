package com.vtech.mobile.kidiconnect2021;

import android.app.Application;
import android.util.Log;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;
import cn.tillusory.sdk.bean.InitStatus;

public class MyApplication extends Application {

    private String TAG = "MyApplication";

    public static final String OFFLINE_AUTH_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();

        // 关闭日志输出
        TiSDK.setLog(false);
        // 离线鉴权
        InitStatus initStatus = TiSDK.initOffline(OFFLINE_AUTH_KEY, getApplicationContext());

        Log.d(TAG,"initStatus : "+initStatus.getMessage());
        // 关闭美颜 API在Ti的官方文档查阅
        TiSDKManager.getInstance().setBeautyEnable(false);
        // 关闭瘦脸
        TiSDKManager.getInstance().setFaceTrimEnable(false);

    }


}
