package com.example.hilt.proxy.httpprocessor;

import java.util.Map;

/**
 * 代理接口
 */
public interface IHttpProcessor {
    void post(String url, Map<String,Object> params, ICallback callback);
    void get(String url, Map<String,Object> params, ICallback callback);
}
