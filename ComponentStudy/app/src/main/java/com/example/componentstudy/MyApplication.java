package com.example.componentstudy;

import android.app.Application;
import android.content.Context;

import androidx.lifecycle.ProcessLifecycleOwner;

import com.example.componentstudy.lifecycle.LifecycleUtil;
import com.example.componentstudy.lifecycle.MyActivityLifeCycleCallBack;

public class MyApplication extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new LifecycleUtil());
        registerActivityLifecycleCallbacks(MyActivityLifeCycleCallBack.getInstance());
    }

    public static Context getContext(){
        return mContext;
    }
}
