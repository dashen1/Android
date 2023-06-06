package com.vtech.mobile.kidiconnect2021;

import android.app.Application;
import android.util.Log;

import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.EffectLoadEngine;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.LocalEffects;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.FileManagerUtil;

import java.io.File;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;

public class MyApplication extends Application {

    private String TAG = "MyApplication";


    public static final String OFFLINE_AUTH_KEY = "";

    @Override
    public void onCreate() {
        super.onCreate();

        FileManagerUtil.initFilePath(this);
        updateLocalEffect();

        // 关闭日志输出
        TiSDK.setLog(false);
        // 离线鉴权
        TiSDK.initOffline(OFFLINE_AUTH_KEY, getApplicationContext());
        // 关闭美颜 API在Ti的官方文档查阅
        TiSDKManager.getInstance().setBeautyEnable(false);
        // 关闭瘦脸
        TiSDKManager.getInstance().setFaceTrimEnable(false);

        //TiSDKManager.getInstance().setMask("CatR");
    }

    private void updateLocalEffect() {
        try {
            LocalEffects factoryEffects = new EffectLoadEngine().readFactoryEffectJson();
            LocalEffects localEffects = new EffectLoadEngine().readLocalEffectJson();

            if (factoryEffects == null
                    || localEffects == null || factoryEffects.getFactoryVersion() <= localEffects.getFactoryVersion()) {
                Log.d(TAG, "不需要更新Mask本地文件");
                return;
            }

            boolean del = FileManagerUtil.deleteDir(new File(FileManagerUtil.getTiDirPath()));
            if (del) {
                Log.d(TAG, "删除旧的Mask文件");
            }
        } catch (Exception e) {
            Log.d(TAG, "更新本地Mask时发生错误:" + e.getMessage());
        }
    }

}
