package com.example.thirdparty.dagger2.example1;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public final class CoffeeLogger {
    private final List<String> logs = new ArrayList<>();

    @Inject
    public CoffeeLogger() {
    }

    public void log(String msg){
        logs.add(msg);
    }

    public List<String> logs(){
        return new ArrayList<>(logs);
    }
}
