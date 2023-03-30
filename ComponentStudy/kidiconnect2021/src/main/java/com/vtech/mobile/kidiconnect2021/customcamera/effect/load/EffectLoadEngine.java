package com.vtech.mobile.kidiconnect2021.customcamera.effect.load;

import android.util.Log;

import com.google.gson.Gson;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.EffectContext;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.EffectModel;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.base.LocalEffects;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask.FacialMask;
import com.vtech.mobile.kidiconnect2021.customcamera.effect.load.facialmask.TiFancyItem;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.FileManagerUtil;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.JsonHelper;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.KCConfigs;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.tillusory.sdk.common.TiUtils;

public class EffectLoadEngine {

    private static final String TAG = "EffectLoadEngine";

    /**
     * 读取出厂自带的Json文件
     */
    public LocalEffects readFactoryEffectJson() throws Exception {
        // 出厂时mask自带的json文件路径
        String factoryJsonPath =
                "mask" + File.separator + FileManagerUtil.LOCAL_EFFECT_JSON_NAME;
        Gson gson = new Gson();
        if (this.getClass().getClassLoader() == null) {
            throw new Exception("readFactoryEffectJson error : classLoader = null ");
        }
        try {
            String jsonStr =
                    FileManagerUtil.readAssetsFileToString(this.getClass().getClassLoader(), factoryJsonPath);
            return gson.fromJson(jsonStr, LocalEffects.class);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            throw new Exception("read Factory effects.json error ");
        }
    }

    /**
     * 读取effect json文件,没有则创建 有则写入
     * /files/tillusory/mask/effects.json
     * effects.json 记录了本地使用的特效列表
     */
    public LocalEffects readLocalEffectJson() throws Exception {

        String localJsonPath = FileManagerUtil.getEffectJsonPath();
        Log.d(TAG, "localJsonPath : " + localJsonPath);
        Gson gson = new Gson();
        if (!FileManagerUtil.isFileExist(localJsonPath)) {
            Log.e(TAG, "read json: json文件不存在");
            File jsonFile = new File(localJsonPath);
            File jsonFileDir = jsonFile.getParentFile();
            Log.e(TAG, "read json: 重新判断，文件存在：" + jsonFile.exists() + " , 目录存在：" + jsonFileDir.exists());
            LocalEffects localEffects = new LocalEffects(new ConcurrentHashMap<>(), "", "", "");
            String jsonStr = gson.toJson(localEffects);
            JsonHelper.writerJsonStr(jsonStr, localJsonPath);
            return localEffects;
        } else {
            try {
                String jsonStr = JsonHelper.readJsonStr(localJsonPath);
                return gson.fromJson(jsonStr, LocalEffects.class);

            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
                FileManagerUtil.deleteFile(localJsonPath);
                FileManagerUtil.deleteFile(FileManagerUtil.getWebEffectJson());
                // 有可能是effects文件损坏 重新加载一份
                if (this.getClass().getClassLoader() != null) {
                    FileManagerUtil.copyAssetsFile(this.getClass().getClassLoader(), "mask/" + FileManagerUtil.LOCAL_EFFECT_JSON_NAME, FileManagerUtil.getMaskPath(), FileManagerUtil.LOCAL_EFFECT_JSON_NAME);
                }
                throw new Exception("read effects.json error ");
            }
        }
    }

    public LocalEffects jsonCompareV2() throws Exception {
        Log.d(TAG, "jsonCompare ----> start request web effect");

        boolean isIconDownloaded = false;
        Gson gson = new Gson();
        String localFacialMaskURL = FileManagerUtil.getMaskPath() + "FacialMask_country/" + KCConfigs.getCountryLanguage() + "/FacialMask.json";

        String remoteJsonStr = CustomDownloadTask.startDownloadJson(KCConfigs.getFacialMaskJsonUrl(), 5);

        // ToDo 如果本地json为空，得到没有列表的容器
        LocalEffects localModel = readLocalEffectJson();

        if (StringUtils.isStringEmptyOrNull(remoteJsonStr)) {
            localModel.setDataHasUpdate(false);
            Log.e(TAG, "jsonCompare ----> remote json string is empty");
            return localModel;
        }
        String localJsonStr = JsonHelper.readJsonStr(localFacialMaskURL);

        FacialMask localFacialMask = gson.fromJson(localJsonStr, FacialMask.class);
        FacialMask remoteFacialMask = gson.fromJson(remoteJsonStr, FacialMask.class);

        Log.d(TAG, "jsonCompare ----> local facialMask file version : " + localFacialMask.getInfo().getFile_version()
                + " effects.json file version " + EffectContext.getFileVersion());

        // 本地FacialMask.json版本
        Double localFileVersion = localFacialMask.getInfo().getFile_version();
        // 远程FacialMask.json版本
        Double remoteFileVersion = remoteFacialMask.getInfo().getFile_version();
        //更新Icon
        Double localIconFileVersion = localFacialMask.getInfo().getIcon_file_version();
        Double remoteIconFileVersion = remoteFacialMask.getInfo().getIcon_file_version();

        // 进行版本判断 effects.json 与 FacialMask.json 的版本号不一致时,也要发起网络请求 则发起网络请求 （使用出厂的FacialMask.json）
        if (EffectContext.getFileVersion() != localFileVersion) {
            localFileVersion = 0.001;
            localIconFileVersion = 0.001;
        } else {
            Log.d(TAG, "jsonCompare ----> local facialMask , effects.json file version consistent ");
        }

        if (localFileVersion < remoteFileVersion) {
            Log.d(TAG, "FacialMask.json : The remote version is inconsistent with the local version , updating data");
            if (localIconFileVersion < remoteIconFileVersion) {
                // TODO: 2020/12/11 Icon下载解包
                isIconDownloaded = loadFacialMaskIconZip(remoteFacialMask.getInfo().getIcon_data().getUrl(),
                        FileManagerUtil.getMaskPath(),
                        FileManagerUtil.getEffectIconPath(),
                        "icon.zip", remoteFacialMask);

                Log.d(TAG, "FacialMask.json : updating  ---> download icon file :" + isIconDownloaded);
            }
            //更新Item
            FacialMask updateFacialMask = compareFacialMask(null, remoteFacialMask, isIconDownloaded);
            LocalEffects remoteModel = updateFacialMask.toKcEffectConfigModel();
            LocalEffects updateModel = compareKcEffectConfigModel(null, remoteModel);

            Log.d(TAG, "FacialMask.json : update complete  ---> new data :" + isIconDownloaded);

            if (isIconDownloaded) {
                // 携带数据有更新的字段 用于后续判断数据更新了
                updateModel.setDataHasUpdate(true);
                updateModel.setFileVersion(remoteFileVersion);
                return updateModel;
            } else {
                localModel.setDataHasUpdate(false);
                return localModel;
            }
        } else {
            // 携带数据有更新的字段 用于后续判断数据没有更新
            localModel.setDataHasUpdate(false);

            Log.d(TAG, "FacialMask.json : The remote version is consistent with the local version , stop update data");
            return localModel;
        }

    }

    private LocalEffects compareKcEffectConfigModel(LocalEffects local, LocalEffects remote) {
        if (local == null) {
            return remote;
        }

        Map<String, EffectModel> localItems = local.getItemList();
        Map<String, EffectModel> remoteItems = remote.getItemList();
        for (Map.Entry<String, EffectModel> e : remoteItems.entrySet()) {
            localItems.put(e.getKey(), e.getValue());
        }
        LocalEffects updateModel = new LocalEffects(localItems, local.getLastMaskName(), local.getLastStickerName(), local.getLastClickType());

        return updateModel;
    }

    private FacialMask compareFacialMask(FacialMask local, FacialMask remote, boolean isIconDownloaded) {

        if (local == null) {
            return remote;
        }
        FacialMask result = new FacialMask();
        List<TiFancyItem> updateMasks = new ArrayList<>();
        List<TiFancyItem> updateStickers = new ArrayList<>();
        List<TiFancyItem> updateCombs = new ArrayList<>();
        //检查Icon文件更新
        result.setInfo(remote.getInfo());

        // mask
        if (local.getMask_list().size() > remote.getMask_list().size()) {
            for (int i = 0; i < remote.getMask_list().size(); i++) {
                updateMasks.add(remote.getMask_list().get(i));
            }
        } else {
            for (int i = 0; i < local.getMask_list().size(); i++) {
                if (local.getMask_list().get(i).getName().equals(remote.getMask_list().get(i).getName())) {
                    if (local.getMask_list().get(i).getVersion() < remote.getMask_list().get(i).getVersion()) {
                        updateMasks.add(remote.getMask_list().get(i));
                    }
                } else {
                    updateMasks.add(remote.getMask_list().get(i));
                }
            }
            for (int i = local.getMask_list().size(); i < remote.getMask_list().size(); i++) {
                updateMasks.add(remote.getMask_list().get(i));
            }
        }

        //sticker
        if (local.getSticker_list().size() > remote.getSticker_list().size()) {
            for (int i = 0; i < remote.getSticker_list().size(); i++) {
                updateStickers.add(remote.getSticker_list().get(i));
            }
        } else {
            for (int i = 0; i < local.getSticker_list().size(); i++) {
                if (local.getSticker_list().get(i).getName().equals(remote.getSticker_list().get(i).getName())) {
                    if (local.getSticker_list().get(i).getVersion() < remote.getSticker_list().get(i).getVersion()) {
                        updateStickers.add(remote.getSticker_list().get(i));
                    }
                } else {
                    updateStickers.add(remote.getSticker_list().get(i));
                }
            }
            for (int i = local.getSticker_list().size(); i < remote.getSticker_list().size(); i++) {
                updateStickers.add(remote.getSticker_list().get(i));
            }
        }

        //comb
        if (local.getComb_list().size() > remote.getComb_list().size()) {
            for (int i = 0; i < remote.getComb_list().size(); i++) {
                updateCombs.add(remote.getComb_list().get(i));
            }
        } else {
            for (int i = 0; i < local.getComb_list().size(); i++) {
                if (local.getComb_list().get(i).getName().equals(remote.getComb_list().get(i).getName())) {
                    if (local.getComb_list().get(i).getVersion() < remote.getComb_list().get(i).getVersion()) {
                        updateCombs.add(remote.getComb_list().get(i));
                    }
                } else {
                    updateCombs.add(remote.getComb_list().get(i));
                }
            }
            for (int i = local.getComb_list().size(); i < remote.getComb_list().size(); i++) {
                updateCombs.add(remote.getComb_list().get(i));
            }
        }
        result.setMask_list(updateMasks);
        result.setSticker_list(updateStickers);
        result.setComb_list(updateCombs);
        return result;
    }

    private boolean loadFacialMaskIconZip(String url, String path, String target, String saveName, FacialMask update) {

        boolean isDownloaded = CustomDownloadTask.startSyncDownload(url, path, saveName, 3);
        if (isDownloaded) {
            // 解压文件
            File zipFile = new File(path + saveName);
            File targetDir = new File(FileManagerUtil.getEffectIconPath());
            if (!targetDir.exists()) {
                boolean ret = targetDir.mkdir();
                Log.d(TAG, "mkdir " + ret);
            }
            TiUtils.unzip(zipFile, targetDir);
            boolean ret = zipFile.delete();
            Log.d(TAG, "delete zip" + ret);
            //更新FacialMask
            updateLocalJsonFile(update);
        }
        return isDownloaded;
    }

    private void updateLocalJsonFile(FacialMask update) {
        Gson gson = new Gson();
        String localFacialMaskURL = FileManagerUtil.getMaskPath() + "/FacialMask_country/" + KCConfigs.getCountryLanguage() + "/FacialMask.json";
        String updateJson = gson.toJson(update);
        try {
            JsonHelper.writerJsonStr(updateJson, localFacialMaskURL);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


}
