package com.example.thirdparty.dagger2.example1;

import javax.inject.Inject;

import dagger.Lazy;

public class CoffeeMaker {
    private final CoffeeLogger logger;
    private final Lazy<Heater> heater;
    private final Pump pump;

    @Inject
    public CoffeeMaker(CoffeeLogger logger, Lazy<Heater> heater, Pump pump) {
        this.logger = logger;
        this.heater = heater;
        this.pump = pump;
    }

    public void brew(){
        heater.get().on();
        pump.pimp();
        logger.log(" coffee! ");
        heater.get().off();
    }
}
