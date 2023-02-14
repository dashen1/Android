package com.example.hilt.base;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.scopes.ActivityScoped;

@InstallIn(ActivityComponent.class)
@Module
public class HttpObjectModule {

    @ActivityScoped
    @Provides
    public HttpObject getHttpObject(){
        return new HttpObject();
    }

}
