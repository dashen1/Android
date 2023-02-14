package com.example.thirdparty.dagger2.example2;

import com.example.thirdparty.dagger2.example2.object.HttpObject;
import com.example.thirdparty.dagger2.example2.scope.AppScope;
import com.example.thirdparty.dagger2.example2.scope.UserScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class HttpModule {

    @AppScope
    @Provides
    HttpObject providerHttpObject(){
        return new HttpObject();
    }
}
