package com.example.thirdparty.dagger2.example1;

import javax.inject.Singleton;

import dagger.Component;

public class CoffeeApp {
    @Singleton
    @Component(modules = {HeaterModule.class,PumpModule.class})
    public interface CoffeeShop{
        CoffeeMaker maker();
        CoffeeLogger logger();
    }

    public static void main(String[] args){
        //CoffeeShop coffeeShop = DaggerCoffeeApp_CoffeeShop.builder().build();
        //coffeeShop.maker().brew();
    }
}
