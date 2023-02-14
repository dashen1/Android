package com.example.thirdparty.dagger2.example2;

import com.example.thirdparty.dagger2.example2.di.MySecondComponent;
import com.example.thirdparty.dagger2.example2.scope.AppScope;

import javax.inject.Singleton;

import dagger.Component;

@AppScope
@Component(modules = {HttpModule.class,DatabaseModule.class},dependencies = {MySecondComponent.class})
public interface MyComponent {

    void injectToMainActivity(Dagger2ExampleActivity activity);
    void injectToSecondActivity(Dagger2ExampleSecondActivity activity);
}
