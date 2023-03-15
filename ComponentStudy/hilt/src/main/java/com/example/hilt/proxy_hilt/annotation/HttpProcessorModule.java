package com.example.hilt.proxy_hilt.annotation;

import com.example.hilt.proxy.httpprocessor.IHttpProcessor;
import com.example.hilt.proxy.httpprocessor.OKHttpProcessor;
import com.example.hilt.proxy.httpprocessor.VolleyProcessor;
import com.example.hilt.proxy.httpprocessor.XUtilsProcessor;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public abstract class HttpProcessorModule {

    @BindOkhttp
    @Binds
    @Singleton
    abstract IHttpProcessor bindOkhttp(OKHttpProcessor okHttpProcessor);

    @BindVolley
    @Binds
    @Singleton
    abstract IHttpProcessor bindVolley(VolleyProcessor volleyProcessor);

    @BindXUtil
    @Binds
    @Singleton
    abstract IHttpProcessor bindXUtils(XUtilsProcessor xUtilsProcessor);

}
