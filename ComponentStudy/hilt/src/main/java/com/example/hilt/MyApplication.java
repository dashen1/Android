package com.example.hilt;

import android.app.Application;

import com.example.hilt.proxy.httpprocessor.HttpHelperProxy;
import com.example.hilt.proxy.httpprocessor.IHttpProcessor;
import com.example.hilt.proxy.httpprocessor.OKHttpProcessor;
import com.example.hilt.proxy_hilt.annotation.BindOkhttp;

import javax.inject.Inject;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class MyApplication extends Application {

    @BindOkhttp
    @Inject
    IHttpProcessor iHttpProcessor;

    @Override
    public void onCreate() {
        super.onCreate();

        HttpHelperProxy.init(new OKHttpProcessor());
    }

    public IHttpProcessor getHttpProcessor() {
        return iHttpProcessor;
    }
}
