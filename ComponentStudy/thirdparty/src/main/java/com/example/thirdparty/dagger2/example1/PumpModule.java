package com.example.thirdparty.dagger2.example1;

import dagger.Binds;
import dagger.Module;

@Module
abstract class PumpModule {

    @Binds
    abstract Pump providePump(Thermosiphon pump);
}
