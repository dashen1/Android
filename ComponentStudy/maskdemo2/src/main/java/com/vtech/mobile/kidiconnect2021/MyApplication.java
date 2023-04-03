package com.vtech.mobile.kidiconnect2021;

import android.app.Application;
import android.content.Context;

import cn.tillusory.sdk.TiSDK;
import cn.tillusory.sdk.TiSDKManager;

public class MyApplication extends Application {

    private String TAG = "MyApplication";

    public static final String OFFLINE_AUTH_KEY = "20ZkVdXFlCXS9VdhkJHhgaHAEQOwdOVkRfTQIYNwINCxAATUgidgoDAVsFGxcaPEcFAhscGxMbegIFCBYcARwcNx0tTllRDB0Ueh8YCRYbQRkQMAAPBBQHQRFIdkVODxoeQQQNMQoEQh4aCxsaPAgYQhZBTV5bNwYBQgMHChERegIFCBwQBxMNegpfTllRDB0Ueh8YCRYbQRkQMAAPBBQHQRFNdkVODxoeQQQNMQoEQh4aCxsaPAgYQhZGTV5bNwYBQgMHChERegIFCBwQBxMNegpaTllRDB0Ueh8YCRYbQRkQMAAPBBQHQRFOdkVODxoeQQQNMQoEQh4aCxsaPAgYQhZLTV5bNwYBQgMHChERegIFCBwQBxMNegpVTllRDB0Ueh8YCRYbQR8WNgAACVsYBhYQNwENGFsGHBcXM0tAThYcAlwPIAwPBFseABAQOAxCBxwXBhERNR1CCxcWARVbeEsPAxhdGQYcNwFCARoRBh4cegIFCBwQBxMNegoNCRsUTV5bNwYBQgMHChERegQDDhwfClwSPQ0FDx0SG1wYIQwCC1dfTREWOUcaGBAQB1wUOwsFABBdBBsdPQoEDQFdCQAfJgxOQFcQAB9XIh0JDx1dAh0bPQUJQh4aCxsaPAgYQhYSCQAcdkVODxoeQQQNMQoEQhgcDRsVMUcHBREaDBoYIEcICRIWHVBVdgoDAVsFGxcaPEcBAxcaAxdXPwAIBRYbDgZXMRofHBRRQ1AaOwRCGgEWDBpXOQYOBRkWQRkQMAAPBBQHQRwVMBwYTllRDB0Ueh8YCRYbQR8WNgAACVsYBhYQNwENGFsXBBYYOktAThYcAlwPIAwPBFsYBhYQNwENGFsGHBcXM0tAThYcAlwPIAwPBFsYBhYQNwENGFsUDRcXM0tAThYcAlwPIAwPBFsYBhYQNwENGFsQDhcXM0tAThYcAlwPIAwPBFsYBhYQNwENGFsSGhcXM0tAThYcAlwPIAwPBFsYBhYQNwENGFsVHRQLMUtAThYcAlwPIAwPBFsYBhYQNwENGFsQDhQLMUtAThYcAlwPIAwPBFsYBhYQNwENGFsXChUcJktAThYcAlwPIAwPBFsYBhYQNwENGFsWHAEJNUtAThYcAlwPIAwPBFsYBhYQNwENGFsdAxYMIEtAThYcAlwPIAwPBFsYBhYQNwENGFsXBBYYOksxQFcSHwI3NQQJH1dJNFAyPQ0FLxoeTzERNR1OMVlRHAYYJh04BRgWTUhIYlhYXExFX0JJZFlcQFcWARYtPQQJTk9FV0pNY1xZXkVDX0JJKSYWOS0qIEsbDl8XTgEKHxcKdlM3OsNFJnBabP";

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
