package com.example.componentstudy.notify;

public class NotificationObject {

    private int notificationID;
    private String channelID;
    private String channelName;

    public NotificationObject(int notificationID, String channelID, String channelName) {
        this.notificationID = notificationID;
        this.channelID = channelID;
        this.channelName = channelName;
    }

    public int getNotificationID() {
        return notificationID;
    }

    public String getChannelID() {
        return channelID;
    }

    public String getChannelName() {
        return channelName;
    }
}
