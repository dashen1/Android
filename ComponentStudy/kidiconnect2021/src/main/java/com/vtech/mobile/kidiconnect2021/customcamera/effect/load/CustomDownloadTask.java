package com.vtech.mobile.kidiconnect2021.customcamera.effect.load;

import android.util.Log;

import com.vtech.mobile.kidiconnect2021.customcamera.utils.DownloadUtil;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.FileManagerUtil;
import com.vtech.mobile.kidiconnect2021.customcamera.utils.StringUtils;

import java.io.File;
import java.nio.charset.StandardCharsets;

public class CustomDownloadTask {

    private static DownloadUtil downloadUtil;

    private static final int timeout = 80;

    public static String startDownloadJson(String url,final int retryCount){
        if (StringUtils.isStringEmptyOrNull(url)){
            return null;
        }

        try {
            long startTime = System.currentTimeMillis();
            byte[] data = null;
            int count = 0;
            while (++count<=retryCount){
                try {
                    data = DownloadUtil.getInstance().download(url);
                    if (data!=null){
                        break;
                    }
                }catch (Exception e){
                    Log.e("CustomDownloadTask", " download failure ");
                }
            }

            if (data ==null){
                Log.e("CustomDownloadTask", " data is null");
                return null;
            }
            Log.d("CustomDownloadTask：", System.currentTimeMillis() - startTime + "");
            return new String(data, StandardCharsets.UTF_8);
        }catch (Exception e){
            Log.e("CustomDownloadTask", " download failure ");
            return null;
        }
    }

    public static boolean startSyncDownload(String url, String saveDirPath, String saveFileName, final int retryCount) {

        if (StringUtils.isStringEmptyOrNull(url) || StringUtils.isStringEmptyOrNull(saveDirPath) || StringUtils.isStringEmptyOrNull(saveFileName)) {
            return false;
        }

        File dirF = new File(saveDirPath);
        if (!dirF.exists()) {
            if (!dirF.mkdirs()) {
                Log.e("CustomDownloadTask：", "saveDirPath not exists");
                return false;
            }
        }

        String savePath = saveDirPath + File.separator + saveFileName;
        try {

            if (downloadUtil == null) {
                downloadUtil = DownloadUtil.getInstance(timeout, timeout);
            }
            if (downloadUtil == null) {
                downloadUtil = DownloadUtil.getInstance(timeout, timeout);
            }

            long startTime = System.currentTimeMillis();
            byte[] data = null;
            int count = 0;
            while (++count <= retryCount) {

                try {
                    data = downloadUtil.download(url);
                    if (data != null) {
                        break;
                    }
                } catch (Exception e) {
                    Log.e("CustomDownloadTask", " download failure ");
                }
            }

            if (data == null) {
                Log.e("CustomDownloadTask", " data is null");
                return false;
            }
            Log.d("CustomDownloadTask：", System.currentTimeMillis() - startTime + "");
            FileManagerUtil.saveFile(data, savePath);
            return true;
        } catch (Exception e) {
            Log.e("CustomDownloadTask", " download failure ");
            return false;
        }
    }
}
