package com.example.componentstudy.notify;

import android.app.NotificationManager;

import androidx.core.app.NotificationCompat;

public class CustomObject {

    private NotificationManager notificationManager;

    private NotificationCompat.Builder builder;

    public CustomObject(NotificationManager notificationManager, NotificationCompat.Builder builder) {
        this.notificationManager = notificationManager;
        this.builder = builder;
    }

    public NotificationManager getNotificationManager() {
        return notificationManager;
    }

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }
}
