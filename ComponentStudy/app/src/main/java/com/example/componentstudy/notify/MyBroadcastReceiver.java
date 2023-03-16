package com.example.componentstudy.notify;

import static android.content.Context.NOTIFICATION_SERVICE;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.componentstudy.R;

public class MyBroadcastReceiver extends BroadcastReceiver {

    static final String RECEIVER = "com.example.MyBroadcastReceiver";
    static final String TAG = "MyBroadcastReceiver";
    public static final String EXTRA_ID = "extra_id";

    @Override
    public void onReceive(Context context, Intent intent) {
        // 1、第一种方式
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            String resultString = remoteInput.getString(NotifyActivity.KEY_TEXT_REPLY);
            // 处理回复内容
            reply(context,resultString);
        }

    }


    private void reply(Context context, String resultString) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(1000);
                Log.d(BroadcastReceiver.class.getName(),resultString);
                onReply(context);
            }
        }).start();
    }

    private void onReply(Context context) {
        final NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 更新通知为“回复成功”
                Notification notification = new NotificationCompat.Builder(context,NotifyActivity.REPLY_CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentText("回复成功")
                        .build();
                notificationManager.notify(1, notification);
            }
        });
    }


}
