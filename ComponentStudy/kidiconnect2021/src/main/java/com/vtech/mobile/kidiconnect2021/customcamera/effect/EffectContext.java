package com.vtech.mobile.kidiconnect2021.customcamera.effect;

import android.util.Log;

import com.google.gson.Gson;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.adapter.ImageAdapter;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.fragment.EffectFragment;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.EffectLoadEngine;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectConstant;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.LocalEffects;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.FileManagerUtil;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.JsonHelper;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.StringUtils;

import java.io.File;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import cn.tillusory.sdk.TiSDKManager;

public class EffectContext {

    private static final String TAG = "EffectContext";

    private static boolean webRequesting;
    /**
     * 这个Map用来存储：本地json文件读到的数据、或服务器下载到新的数据
     */
    protected static final Map<String, EffectModel> modelMap = new ConcurrentHashMap<>();

    private static Map<String, Integer> downloadMap;
    private static ImageAdapter effectAdapter;

    /**
     * 最新选择的Mask特效
     */
    private static String maskNameLatest = "";
    /**
     * 最新选择的Sticker特效
     */
    private static String stickerNameLatest = "";

    /**
     * 最新选择的类型：mask / sticker / comb
     */
    private static String clickedType = "";

    private static EffectFragment currentOpenFragment;

    public static boolean isClose() {
        return currentOpenFragment == null;
    }

    /**
     * 服务器资源版本
     */
    private static double fileVersion = 0.001;
    /**
     * 出厂json版本
     */
    private static double factoryVersion = 0.001;

    public static double getFileVersion() {
        return fileVersion;
    }

    public static void setFileVersion(double fileVersion) {
        EffectContext.fileVersion = fileVersion;
    }

    protected final static List<EffectModel> modelList = new ArrayList<>(30);


    public static List<EffectModel> getModelList() {
        return modelList;
    }

    public static boolean isWebRequesting() {
        return webRequesting;
    }

    public static void setWebRequesting(boolean webRequesting) {
        EffectContext.webRequesting = webRequesting;
    }

    public static String getMaskNameLatest() {
        return maskNameLatest;
    }

    public static void setMaskNameLatest(String maskNameLatest) {
        EffectContext.maskNameLatest = maskNameLatest;
    }

    public static String getStickerNameLatest() {
        return stickerNameLatest;
    }

    public static void setStickerNameLatest(String stickerNameLatest) {
        EffectContext.stickerNameLatest = stickerNameLatest;
    }

    public static String getClickedType() {
        return clickedType;
    }

    public static void setClickedType(String type) {
        clickedType = type;
    }
    /**
     * 重置版本号
     */
    private static void resetFileVersion() {
        fileVersion = 0.001;
    }

    public static void notifyOpenEffectFragment(EffectFragment fragment) {
        // 打开列表时，创建的EffectFragment对象会赋值给currentOpenFragment这个变量
        // 关闭列表时，currentOpenFragment会被设置为null
        // 我们可以通过currentOpenFragment是否等于null，来判断列表是否打开
        currentOpenFragment = fragment;
        initLocalList();
    }

    public static void notifyCloseEffectFragment() {
        // 列表都关闭了，特效随之关闭
        EffectUtils.cancelEffectShow(false);
        if (currentOpenFragment != null) {

            // 关闭EffectFragment时，如果downloadMap中没有数据，则将downloadMap设置为null
            if (downloadMap != null && downloadMap.size() == 0) {
                downloadMap = null;
            }

            // 如果modelMap中存在数据 则将数据进行持久化，再清空容器
            if (modelMap.size() != 0) {
                try {
                    // 这个方法主要是把数据写入Json文件
                    writeEffectDataToFileWhileOpen();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                modelMap.clear();
            }

            modelList.clear();
            currentOpenFragment = null;
            effectAdapter = null;
        }
    }

    public static void writeEffectDataToFileWhileOpen() throws Exception {
        if (!isClose() && modelMap.size() != 0) {
            LocalEffects localEffects = new LocalEffects(modelMap, maskNameLatest, stickerNameLatest, clickedType);
            localEffects.setFileVersion(fileVersion);
            localEffects.setFactoryVersion(factoryVersion);
            Gson gson = new Gson();
            // 避免空列表覆盖到json
            if (localEffects.getItemList().size() == 0) {
                Log.e(TAG, "writeEffectDataToFileWhileOpen ： size <= 0");
                return;
            }
            String dataToWrite = gson.toJson(localEffects);
            JsonHelper.writerJsonStr(dataToWrite, FileManagerUtil.getEffectJsonPath());
        }
    }

    public static void initLocalList(){
        LocalEffects dataInstance = null;

        try {
            dataInstance = new EffectLoadEngine().readLocalEffectJson();
        }catch (Exception e){
            Log.e(TAG, "open list: 读取json文件时错误: " + e.getMessage());
        }

        try {
            // 兜底方案 ： 如果Json文件丢失，则读取安装包中（代码中assets目录）出厂的effects.json
            // 这么做的原因：如果设备存储目录的effects.json文件丢失，就采用出厂的文件，避免读不到
            if (dataInstance == null || dataInstance.getItemList().size() == 0) {
                dataInstance = new EffectLoadEngine().readFactoryEffectJson();
            }

            // 在我们effects.json中，有两个字段‘fileVersion’以及‘factoryVersion’
            // fileVersion代表服务器资源的版本号，我们通过这个版本号判断是否需要下载新的
            // factoryVersion代表出厂版本号，当覆盖安装app时，如果发现新安装包的版本号比目前的大，则使用新包的，就是通过这个版本号来校验
            fileVersion = dataInstance.getFileVersion();
            factoryVersion = dataInstance.getFactoryVersion();
            Log.d(TAG, "open list: factoryVersion:" + factoryVersion);
            // 要装载数据前，先把Map清空一下，避免存在脏数据
            modelMap.clear();
            // 装配从json读到的数据到modelMap
            Set<Map.Entry<String, EffectModel>> entrySet = dataInstance.getItemList().entrySet();
            for (Map.Entry<String, EffectModel> entry : entrySet) {
                Log.d(TAG,"url : "+entry.getValue().getUrl());
                modelMap.put(entry.getKey(),entry.getValue());
            }
            // json文件会记录上次使用的特效，我们读取出来，用来恢复上次使用的特效
            maskNameLatest = dataInstance.getLastMaskName();
            stickerNameLatest = dataInstance.getLastStickerName();
            clickedType = dataInstance.getLastClickType();
            if (StringUtils.isStringEmptyOrNull(clickedType)) {
                clickedType = EffectConstant.TYPE_MASK;
            }
            // 过滤不合法数据 ToDo 暂时有点不理解
            iconFilter(modelMap);
            // 把modelMap中读到的数据进行排序
            List<EffectModel> models = orderList(modelMap);
            Log.d(TAG,"models : "+models.size());
            // 排序后的数据装载到modelList中
            modelList.clear();
            modelList.addAll(models);
        }catch (Exception e){
            Log.e(TAG, "open list: 加载本地json数据错误：" + e.getMessage());
        }

    }

    /**
     *  按照EffectModel的序号进行排序
     * @param targetMap
     * @return
     */
    private static List<EffectModel> orderList(Map<String, EffectModel> targetMap) {
        if (targetMap==null||targetMap.size()==0){
            return null;
        }
        Log.d(TAG, "orderList ---> Order Effect Model List");
        ArrayList<Integer> orderArrayList = new ArrayList<>(targetMap.size());
        HashMap<Integer, EffectModel> orderModelMap = new HashMap<>(targetMap.size());
        Set<Map.Entry<String, EffectModel>> modelEntrySet = targetMap.entrySet();
        for (Map.Entry<String, EffectModel> modelEntry : modelEntrySet) {
            int order = modelEntry.getValue().getOrder();
            if (orderModelMap.containsKey(order)){
                Log.e(TAG, "存在mask顺序相同导致冲突,忽略掉该mask：" + modelEntry.getValue().getName());
                continue;
            }
            orderArrayList.add(order);
            orderModelMap.put(order,modelEntry.getValue());
        }
        Collections.sort(orderArrayList);
        List<EffectModel> orderModelList = new ArrayList<>(orderArrayList.size());
        for (Integer key : orderArrayList) {
            orderModelList.add(orderModelMap.get(key));
        }
        return orderModelList;
    }

    private static void iconFilter(Map<String, EffectModel> targetMap) {
        Iterator<Map.Entry<String, EffectModel>> entryIterator = targetMap.entrySet().iterator();
        while (entryIterator.hasNext()) {
            EffectModel model = entryIterator.next().getValue();
            if (!new File(model.getThumbPath()).exists()){
                if (model.isEmptyType()){
                    // 确保基本的icon不丢失
                    try {
                        FileManagerUtil.copyAssetsFile(
                                model.getClass().getClassLoader(),
                                "mask/icon/"+model.getType()+"_icon/"+model.getThumb(),
                                FileManagerUtil.getMaskPath()+"/icon/"+model.getType()+"_icon",
                                model.getThumb()
                        );
                    }catch (Exception e){
                        Log.e(TAG, "copy icon error " + model.getName());
                    }
                }else {
                    entryIterator.remove();
                    resetFileVersion();
                    Log.e(TAG, "iconFilter ---> Icon not exists : " + model.getName());
                }
            }
        }
    }

    public static LocalEffects requestWebEffectList(){
        try {
            LocalEffects tmpEffects = new EffectLoadEngine().jsonCompareV2();
            if (tmpEffects.isDataHasUpdate()){
                Log.d(TAG, "web effect has update data" + tmpEffects.getItemList().size());
                return tmpEffects;
            }else {
                Log.d(TAG, "web effect no update data");
                return null;
            }
        }catch (Exception e){
            Log.e(TAG, "requestWebEffectList ---> request web effect error : " + e.getMessage());
            return null;
        }
    }


    public static void updateRemoteHandle(LocalEffects remoteModelData) {

        double tempVersion = fileVersion;
        // 由于下载了新数据，现在使用新数据的版本号
        setFileVersion(remoteModelData.getFileVersion());
        Log.d(TAG, "updateRemoteHandle : start handle remoteModelData..");

        // remoteModelMap里面保存着新数据
        Map<String, EffectModel> remoteModelMap = remoteModelData.getItemList();
        // 将这波数据进行过滤，去除不需要的数据
        iconFilter(remoteModelMap);
        // 根据版本好过滤这些新数据
        remoteF(modelMap, remoteModelMap);
        // 看看这批数据中有没有 ‘Empty Mask’
        EffectModel tmpEmpty = modelMap.get(EffectModel.EMPTY_EFFECT_NAME);
        // 缺少‘Empty Mask’，则补充进去
        if (!modelMap.containsKey(EffectModel.EMPTY_EFFECT_NAME) && tmpEmpty != null) {
            modelMap.put(EffectModel.EMPTY_EFFECT_NAME, tmpEmpty);
        }
        // 将这批数据进行排序
        List<EffectModel> modelList = orderList(modelMap);
        // ToDo
    }

    private static void remoteF(Map<String, EffectModel> modelMap, Map<String, EffectModel> remoteModelMap) {


    }

    public static void startToShowEffect(EffectModel model) throws Exception {
        if (isClose())
            return;
        try {
            if (model.isFileAvailable()) {
                if (model.isMask()) {
                    Log.d(TAG,"===========================");
                    Log.d(TAG,"model.getName() : "+model.getName());
                    Log.d(TAG,"model type : "+model.getType());
                    Log.d(TAG,"============================");
                    TiSDKManager.getInstance().setMask(model.getName());
                    TiSDKManager.getInstance().setSticker("");
                    maskNameLatest = model.getName();
                    stickerNameLatest = "";
                    clickedType = EffectConstant.TYPE_MASK;
                } else if (model.isSticker()) {
                    Log.d(TAG,"===========================");
                    Log.d(TAG,"model.getName() : "+model.getName());
                    Log.d(TAG,"model type : "+model.getType());
                    Log.d(TAG,"============================");
                    TiSDKManager.getInstance().setSticker(model.getName());
                    TiSDKManager.getInstance().setMask("");
                    maskNameLatest = "";
                    stickerNameLatest = model.getName();
                    clickedType = EffectConstant.TYPE_STICKER;
                } else if (model.isComb()) {
                    Log.d(TAG,"===========================");
                    Log.d(TAG,"model.getName() : "+model.getName());
                    Log.d(TAG,"model type : "+model.getType());
                    Log.d(TAG,"============================");
                    TiSDKManager.getInstance().setSticker(model.getName());
                    TiSDKManager.getInstance().setMask(model.getName());
                    maskNameLatest = model.getName();
                    stickerNameLatest = model.getName();
                    clickedType = EffectConstant.TYPE_COMB;
                }
            } else {
                Log.e(TAG, "effect error : file " + model.getName() + " not exists");
                // TODO: 2021/6/8 弹出失败提示
            }
        } catch (Exception e) {
            EffectUtils.cancelEffectShow(true);
            throw new Exception(e.getMessage());
        }
    }

}
