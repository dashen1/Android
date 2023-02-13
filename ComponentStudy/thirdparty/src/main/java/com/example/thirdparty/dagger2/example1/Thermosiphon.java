package com.example.thirdparty.dagger2.example1;

import javax.inject.Inject;

public class Thermosiphon implements Pump{

    private final CoffeeLogger logger;
    private final Heater heater;

    @Inject
    public Thermosiphon(CoffeeLogger logger, Heater heater) {
        this.logger = logger;
        this.heater = heater;
    }

    @Override
    public void pimp() {
        if (heater.isHot()){
            logger.log("==> pumping ==>");
        }
    }
}
