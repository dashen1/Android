package com.example.componentstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocalBroadcastReceiver extends BroadcastReceiver {

    public static final String Local_Receiver_Name = "com.example.ReceiverLocal";

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.d("LocalBroadcastReceiver","LocalBroadcastReceiver receive broadcast.");
    }
}
