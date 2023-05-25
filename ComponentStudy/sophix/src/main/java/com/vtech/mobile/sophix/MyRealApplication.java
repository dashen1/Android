package com.vtech.mobile.sophix;

import android.app.Application;
import android.util.Log;

public class MyRealApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Log.i("MainApplication", "原有的Application，可以通过补丁修改");
    }

}
