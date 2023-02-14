package com.example.hilt.proxy.httpprocessor;

public interface ICallback {
    void onSuccess(String result);
    void onFailure(String e);
}
