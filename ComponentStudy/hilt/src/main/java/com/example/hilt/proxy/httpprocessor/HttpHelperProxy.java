package com.example.hilt.proxy.httpprocessor;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

public class HttpHelperProxy implements IHttpProcessor {

    public HttpHelperProxy() {
    }

    // 定义被代理者
    private static IHttpProcessor mIHttpProcessor = null;

    public static void init(IHttpProcessor httpProcessor) {
        mIHttpProcessor = httpProcessor;
    }

    private static HttpHelperProxy instance;

    public static HttpHelperProxy obtain() {
        synchronized (HttpHelperProxy.class) {
            if (instance == null) {
                instance = new HttpHelperProxy();
            }
        }
        return instance;
    }

    @Override
    public void post(String url, Map<String, Object> params, ICallback callback) {
        // String finalUrl = appendParams(url, params);
        mIHttpProcessor.post(url, params, callback);
    }

    private String appendParams(String url, Map<String, Object> params) {
        if (params == null || params.isEmpty()) {
            return url;
        }
        StringBuilder urlBuilder = new StringBuilder(url);
        if (urlBuilder.indexOf("?") <= 0) {
            urlBuilder.append("?");
        } else {
            if (!urlBuilder.toString().endsWith("?")) {
                urlBuilder.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            urlBuilder.append("&" + entry.getKey())
                    .append("=")
                    .append(encode(entry.getValue().toString()));
        }
        return urlBuilder.toString();
    }

    private static String encode(String str) {
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    public void get(String url, Map<String, Object> params, ICallback callback) {

    }
}
