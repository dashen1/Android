package com.example.componentstudy.lifecycle;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MyActivityLifeCycleCallBack implements Application.ActivityLifecycleCallbacks {

    private final ArrayList<Activity> activityList = new ArrayList<>();

    private volatile static MyActivityLifeCycleCallBack instance;

    public static MyActivityLifeCycleCallBack getInstance(){
        if (instance ==null){
            synchronized (MyActivityLifeCycleCallBack.class){
                if (instance==null){
                    instance = new MyActivityLifeCycleCallBack();
                }
            }
        }
        return instance;
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        activityList.add(activity);
        System.out.println("onActivityCreated:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        System.out.println("onActivityStarted:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        System.out.println("onActivityResumed:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        System.out.println("onActivityPaused:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        System.out.println("onActivityStopped:"+activity.getLocalClassName());
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        System.out.println("onActivitySaveInstanceState:"+activity.getLocalClassName());
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        activityList.remove(activity);
        System.out.println("onActivityDestroyed:"+activity.getLocalClassName());
    }

    public void clearAllActivity(){
//        for (int i = 0; i < activityList.size(); i++) {
//            if (activityList.size() != i+1){
//                activityList.get(i).finish();
//            }
//        }
//        if (activityList.size()>0){
//            for (Activity activity : activityList) {
//                activity.finish();
//            }
//        }
    }
}
