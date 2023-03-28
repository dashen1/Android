package com.example.opengl;

import android.app.Application;

import com.example.opengl.customcamera.TiAuth;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // 关闭日志输出
        TiSDK.setLog(false);
        // 离线鉴权
        TiSDK.initOffline(TiAuth.OFFLINE_AUTH_KEY, getApplicationContext());
        // 关闭美颜 API在Ti的官方文档查阅
        TiSDKManager.getInstance().setBeautyEnable(false);
        // 关闭瘦脸
        TiSDKManager.getInstance().setFaceTrimEnable(false);
    }

}
