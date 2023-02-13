package com.example.thirdparty.dagger2.example1;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module
interface HeaterModule {

    @Binds
    @Singleton
    Heater bindHeater(ElectricHeater impl);
}
