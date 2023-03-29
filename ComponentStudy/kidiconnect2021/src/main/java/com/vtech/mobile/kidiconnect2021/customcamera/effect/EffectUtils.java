package com.vtech.mobile.kidiconnect2021.customcamera.effect;

import android.content.Context;
import android.util.Log;

import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.UIHelper;

import cn.tillusory.sdk.TiSDKManager;

public class EffectUtils {

    public static int getItemSize(Context context, int itemCount) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        return widthPixels / itemCount;
    }

    public static int getItemSizeComp(Context context, int itemCount, int dpValue) {
        int widthPixels = context.getResources().getDisplayMetrics().widthPixels;
        int size = widthPixels / itemCount;
        int pi = UIHelper.dip2px(context, dpValue);
        if (size > pi) {
            size = pi - 2;
        }
        return size;
    }

    public static void setEffect(EffectModel model) {
        Log.d("setEffect", "setEffect");
        TiSDKManager.getInstance().setMask(model.getName());
    }
}
