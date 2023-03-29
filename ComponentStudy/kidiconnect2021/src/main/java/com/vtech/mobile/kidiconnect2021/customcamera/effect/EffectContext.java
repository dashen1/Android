package com.vtech.mobile.kidiconnect2021.customcamera.effect;

import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;

import java.util.ArrayList;
import java.util.List;

public class EffectContext {

    private static final String TAG = "EffectContext";

    protected final static List<EffectModel> modelList = new ArrayList<>(30);


    public static List<EffectModel> getModelList() {
        return modelList;
    }


}
