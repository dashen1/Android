package com.example.thirdparty.dagger2.example2;

import com.example.thirdparty.dagger2.example2.object.DatabaseObject;
import com.example.thirdparty.dagger2.example2.scope.AppScope;
import com.example.thirdparty.dagger2.example2.scope.UserScope;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @UserScope
    @Provides
    DatabaseObject providerDatabaseObject(){
        return new DatabaseObject();
    }
}
