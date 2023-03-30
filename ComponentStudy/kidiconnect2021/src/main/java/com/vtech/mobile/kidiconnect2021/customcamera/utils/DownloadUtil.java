package com.vtech.mobile.kidiconnect2021.customcamera.utils;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadUtil {
    private static final String TAG = "DownloadUtil";
    private static DownloadUtil downloadUtilWithDefaultTimeout;
    private static DownloadUtil downloadUtilWithTimeOut;
    private final OkHttpClient okHttpClient;
    private final int readTimeout;
    private final int writeTimeout;

    private DownloadUtil() {
        this(10, 30, 10);
    }

    public DownloadUtil(int connectTimeout, int readTimeout, int writeTimeout) {
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
        this.okHttpClient = null;
    }

    public static DownloadUtil getInstance() {
        if (downloadUtilWithDefaultTimeout == null) {
            synchronized (DownloadUtil.class) {
                if (downloadUtilWithDefaultTimeout == null) {
                    downloadUtilWithDefaultTimeout = new DownloadUtil();
                }
            }
        }
        return downloadUtilWithDefaultTimeout;
    }

    public static DownloadUtil getInstance(int readTimeout, int writeTimeout) {
        if (downloadUtilWithTimeOut == null) {
            synchronized (DownloadUtil.class) {
                if (downloadUtilWithTimeOut == null) {
                    downloadUtilWithTimeOut = new DownloadUtil(readTimeout, readTimeout, writeTimeout);
                }
            }
        } else if (downloadUtilWithTimeOut.readTimeout != readTimeout || downloadUtilWithTimeOut.readTimeout != writeTimeout) {
            downloadUtilWithTimeOut = new DownloadUtil(readTimeout, readTimeout, writeTimeout);
        }

        return downloadUtilWithTimeOut;
    }

    public byte[] download(String urlStr) throws IOException {
        byte[] byt = null;
        Response response  =null;
        try {
            Request request = new Request.Builder().url(urlStr).build();
            Call call = this.okHttpClient.newCall(request);
            response = call.execute();
            if (response.code()==200){
                Log.d("DownloadUtil", "download success:");
                byt = response.body().bytes();
            }
            Log.d("DownloadUtil", "download responseCode:" + response.code() + " message:" + response.message());
        }catch (Exception e){
            Log.e("DownloadUtil", "download", e);
            throw e;
        }finally {
            if (response != null) {
                response.body().close();
            }
        }
        return byt;
    }
}
