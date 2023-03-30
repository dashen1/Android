package com.vtech.mobile.kidiconnect2021.BackStage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BackStageService {

    private final String TAG = "BackstageService";

    private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private static volatile BackStageService instance;

    public BackStageService() {
    }

    public static BackStageService getInstance(){
        if(instance==null){
            synchronized (BackStageService.class){
                if (instance==null){
                    instance = new BackStageService();
                }
            }
        }
        return instance;
    }


    public ExecutorService getThreadPool() {
        return threadPool;
    }
}
