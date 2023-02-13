package com.example.thirdparty.dagger2.example1;

import javax.inject.Inject;

public class ElectricHeater implements Heater{

    private final CoffeeLogger logger;
    private boolean heating;

    @Inject
    public ElectricHeater(CoffeeLogger logger) {
        this.logger = logger;
    }

    @Override
    public void on() {
        this.heating = true;
        logger.log("===heating===");
    }

    @Override
    public void off() {
        this.heating = false;
    }

    @Override
    public boolean isHot() {
        return heating;
    }
}
