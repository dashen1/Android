package com.example.componentstudy.service;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.R;

public class ServiceActivity extends AppCompatActivity {

    private static final String TAG = "ServiceActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        Log.d(TAG,"onCreate");
    }


    public void onStartService(View view) {
        Intent intent = new Intent(ServiceActivity.this, MyService.class);
        startService(intent);
        ChildThread childThread = new ChildThread();
        Thread thread = new Thread(childThread);
        thread.start();
    }

    public void onStopService(View view) {
        Intent intent = new Intent(ServiceActivity.this, MyService.class);
        stopService(intent);
    }


    class ChildThread implements Runnable{

        private volatile boolean isFlag = true;

        @Override
        public void run() {
            while (isFlag){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Log.d(TAG,"exception:"+ e);
                }
                Log.d(TAG,"main thread is running on background!");
            }
        }
    }
}
