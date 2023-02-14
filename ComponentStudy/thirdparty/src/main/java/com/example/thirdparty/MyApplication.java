package com.example.thirdparty;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.thirdparty.dagger2.example2.DatabaseModule;
import com.example.thirdparty.dagger2.example2.HttpModule;
import com.example.thirdparty.dagger2.example2.MyComponent;

public class MyApplication extends Application {

    private MyComponent myComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        ARouter.init(this);
//        myComponent = DaggerMyComponent.builder()
//                .httpModule(new HttpModule())
//                .databaseModule(new DatabaseModule())
//                .mySecondComponent(DaggerMySecondComponent.create())
//                .build();
    }

    public MyComponent getAppComponent(){
        return myComponent;
    }

}
