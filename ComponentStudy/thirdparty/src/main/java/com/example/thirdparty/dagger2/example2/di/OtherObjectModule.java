package com.example.thirdparty.dagger2.example2.di;

import com.example.thirdparty.dagger2.example2.scope.AppScope;
import com.example.thirdparty.dagger2.example2.scope.UserScope;

import dagger.Module;
import dagger.Provides;

@Module
public class OtherObjectModule {

    @UserScope
    @Provides
    OtherObject providerOtherObject(){
        return new OtherObject();
    }
}
