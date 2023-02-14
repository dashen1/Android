package com.example.thirdparty.dagger2.example2.di;

import com.example.thirdparty.dagger2.example2.scope.UserScope;

import javax.inject.Singleton;

import dagger.Component;

@UserScope
@Component(modules = {OtherObjectModule.class})
public interface MySecondComponent {

    OtherObject providerOtherObject();
}
