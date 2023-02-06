package com.example.componentstudy.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GlobalBroadcastReceiver extends BroadcastReceiver {

    public static final String Global_Receiver_Name = "com.example.ReceiverGlobal";

    public GlobalBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("CustomBroadcastReceiver","GlobalBroadcastReceiver receive broadcast.");
    }
}
