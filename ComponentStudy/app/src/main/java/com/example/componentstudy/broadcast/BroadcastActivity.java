package com.example.componentstudy.broadcast;

import static com.example.componentstudy.broadcast.GlobalBroadcastReceiver.Global_Receiver_Name;
import static com.example.componentstudy.broadcast.LocalBroadcastReceiver.Local_Receiver_Name;

import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.componentstudy.R;

public class BroadcastActivity extends AppCompatActivity {


    private GlobalBroadcastReceiver globalBroadcastReceiver;
    private LocalBroadcastReceiver localBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast);

    }

    @Override
    protected void onResume() {
        super.onResume();
        globalBroadcastReceiver = new GlobalBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Global_Receiver_Name);
        registerReceiver(globalBroadcastReceiver, intentFilter);
        Log.d("BroadcastActivity", "register receiver broadcast dynamically.");

        localBroadcastReceiver = new LocalBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(Local_Receiver_Name);
        LocalBroadcastManager.getInstance(BroadcastActivity.this).registerReceiver(localBroadcastReceiver, intentFilter);
        Log.d("BroadcastActivity", "register receiver broadcast dynamically.");
    }

    public void startGlobalBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction(Global_Receiver_Name);
        intent.setComponent(new ComponentName(getPackageName(),"com.example.componentstudy.broadcast.GlobalBroadcastReceiver"));
        sendBroadcast(intent);
        Log.d("BroadcastActivity", "sendBroadcast");
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(globalBroadcastReceiver);
        LocalBroadcastManager.getInstance(BroadcastActivity.this).unregisterReceiver(localBroadcastReceiver);
        Log.d("BroadcastActivity", "onPause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("BroadcastActivity", "onDestroy");
    }

    public void startLocalBroadcast(View view) {
        Intent intent = new Intent();
        intent.setAction(Local_Receiver_Name);
        intent.setComponent(new ComponentName(getPackageName(),"com.example.componentstudy.broadcast.LocalBroadcastReceiver"));
        LocalBroadcastManager.getInstance(BroadcastActivity.this).sendBroadcast(intent);
        Log.d("BroadcastActivity", "sendBroadcast");
    }
}
