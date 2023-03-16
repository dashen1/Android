package com.example.componentstudy.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.RemoteInput;

import com.example.componentstudy.R;
import com.example.componentstudy.lifecycle.LifecycleActivity;
import com.example.componentstudy.notify.service.MediaPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotifyActivity extends AppCompatActivity {

    @BindView(R.id.btn_play_or_pause)
    Button btn_play_or_pause;
    @BindView(R.id.btn_test)
    Button btn_test;
    @BindView(R.id.btn_reply)
    Button btn_reply;
    @BindView(R.id.btn_download)
    Button btn_download;
    @BindView(R.id.btn_big_text)
    Button btn_big_text;
    @BindView(R.id.btn_list_view)
    Button btn_list_view;
    @BindView(R.id.btn_dialog)
    Button btn_dialog;
    @BindView(R.id.btn_media)
    Button btn_media;

    int clickCount = 0;

    public final static int MUSIC_NOTIFICATION_ID = 1;
    public final static String MUSIC_CHANNEL_ID = "001";
    public final static String MUSIC_CHANNEL_NAME = "music";

    public final static int REPLY_NOTIFICATION_ID = 2;
    public final static String REPLY_CHANNEL_ID = "002";
    public final static String REPLY_CHANNEL_NAME = "reply";

    public final static int DOWNLOAD_NOTIFICATION_ID = 3;
    public final static String DOWNLOAD_CHANNEL_ID = "003";
    public final static String DOWNLOAD_CHANNEL_NAME = "download";

    public final static int BIG_TEXT_NOTIFICATION_ID = 4;
    public final static String BIG_TEXT_CHANNEL_ID = "004";
    public final static String BIG_TEXT_CHANNEL_NAME = "bigText";

    public final static int LIST_VIEW_NOTIFICATION_ID = 5;
    public final static String LIST_VIEW_CHANNEL_ID = "005";
    public final static String LIST_VIEW_CHANNEL_NAME = "listView";

    public final static int DIALOG_NOTIFICATION_ID = 6;
    public final static String DIALOG_CHANNEL_ID = "006";
    public final static String DIALOG_CHANNEL_NAME = "dialog";

    private long mNotificationPostTime = 0;

    final static String KEY_TEXT_REPLY = "key_text_reply";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        ButterKnife.bind(this);

        MyBroadcastReceiver br = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(MyBroadcastReceiver.TAG);
        registerReceiver(br, intentFilter);

    }

    @OnClick({R.id.btn_play_or_pause, R.id.btn_test, R.id.btn_reply, R.id.btn_download, R.id.btn_big_text, R.id.btn_list_view,
            R.id.btn_dialog,R.id.btn_media})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_play_or_pause:
                playOrPauseMedia();
                break;
            case R.id.btn_test:
                musicPlayBackgroundNotificationTest();
                break;
            case R.id.btn_reply:
                replyNotification();
                break;
            case R.id.btn_download:
                downloadNotification();
                break;
            case R.id.btn_big_text:
                bigTextNotification();
                break;
            case R.id.btn_list_view:
                listViewNotification();
                break;
            case R.id.btn_dialog:
                dialogNotification();
                break;
            case R.id.btn_media:
                mediaNotification();
                break;
        }
    }

    private void playOrPauseMedia() {
        if (clickCount % 2 == 0) {
            // start
            MediaPlayer.playOrPause(true);
        } else {
            // stop
            MediaPlayer.playOrPause(false);
        }
        clickCount++;
    }

    private void listViewNotification() {
        NotificationObject object = new NotificationObject(LIST_VIEW_NOTIFICATION_ID, LIST_VIEW_CHANNEL_ID, LIST_VIEW_CHANNEL_NAME);
        CustomObject customObject = customNotification(object);
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle("BigContentTitle:");
        for (int i = 0; i < 6; i++) {
            inboxStyle.addLine("item " + i);
        }
        Notification notification = customObject.getBuilder().setStyle(inboxStyle).build();
        customObject.getNotificationManager().notify(object.getNotificationID(), notification);
    }

    private void mediaNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(new Intent(getApplicationContext(), MediaPlayerServiceTest.class));
        } else {
            startService(new Intent(getApplicationContext(), MediaPlayerServiceTest.class));
        }
    }

    private void dialogNotification() {
        NotificationObject object = new NotificationObject(DIALOG_NOTIFICATION_ID, DIALOG_CHANNEL_ID, DIALOG_CHANNEL_NAME);
        CustomObject customObject = customNotification(object);

        NotificationCompat.MessagingStyle.Message message1 =
                new NotificationCompat.MessagingStyle.Message("How are you?", System.currentTimeMillis(), "Tom");

        NotificationCompat.MessagingStyle.Message message2 =
                new NotificationCompat.MessagingStyle.Message("I am fine, how about you?", System.currentTimeMillis(), "Jack");

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        NotificationCompat.Builder builder = customObject.getBuilder();
        Notification notification = builder.setStyle(new NotificationCompat.MessagingStyle("Mike")
                        .addMessage(message1)
                        .addMessage(message2))
                .setLargeIcon(bitmap)
                .build();
        customObject.getNotificationManager().notify(object.getNotificationID(), notification);
    }

    private CustomObject customNotification(NotificationObject object) {
        // 1、Android 8.0 及以上需要设置 channel
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(object.getChannelID(), object.getChannelName(), NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, object.getChannelID());
        builder.setSmallIcon(R.drawable.ic_notification);
        return new CustomObject(notificationManager, builder);
    }

    private void bigTextNotification() {
        String bigText = "A notification is a message that Android displays outside your app's UI to provide the user with reminders, communication from other people, or other timely information from your app. Users can tap the notification to open your app or take an action directly from the notification.";

        // 1、Android 8.0 及以上需要设置 channel
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(BIG_TEXT_CHANNEL_ID, BIG_TEXT_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // 2、构建 notification
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, BIG_TEXT_CHANNEL_ID);
        builder.setContentTitle("大文本/大图片通知")
                .setContentText("Much longer text that cannot fit one line...")
                // 大文本
                //.setStyle(new NotificationCompat.BigTextStyle().bigText(bigText))
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(bitmap)
                // 这样大图标就会在展开的时候消失
                .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap)
                        .bigLargeIcon(null))
                .setAutoCancel(true);
        Notification notification = builder.build();
        // 3、发送通知
        notificationManager.notify(BIG_TEXT_NOTIFICATION_ID, notification);
    }

    private void musicPlayBackgroundNotificationTest() {
        //高版本的模拟器或手机还需要开启渠道才能显示通知
        // 1、自定义通知布局
        RemoteViews remoteViews;
        final String albumName = "七里香";
        final String artistName = "周杰伦";
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, "随心听");
        remoteViews.setTextViewText(R.id.text, text);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        final Bitmap bitmap = scaleBitmap(b, 160, 160);
        // final Bitmap bitmap = ImageUtils.getArtworkQuick(this, R.drawable.funny, 160, 160);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.image, bitmap);
        }

        // 2、Android 8.0 以上需要设置 channel
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(MUSIC_CHANNEL_ID, MUSIC_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            notificationChannel.setLightColor(Color.YELLOW); //小红点颜色
            notificationChannel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // 3、实例化一个意图，当点击通知时会跳转执行这个意图
        Intent intent = new Intent(this, LifecycleActivity.class);
        // 4、将意图进行封装
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }

        RemoteViews bigContentView = new RemoteViews(getPackageName(), R.layout.notification_big);
        bigContentView.setTextViewText(R.id.text_expanded, text);
        bigContentView.setImageViewBitmap(R.id.image_expanded, bitmap);
        // 5、构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MUSIC_CHANNEL_ID);
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                //.setTicker("Ticker")
                .setWhen(System.currentTimeMillis())
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigContentView)
                .setDefaults(Notification.PRIORITY_DEFAULT); // 想要悬浮出来， 这里必须要设置
        Notification notification = builder.build();

        //自定义bigContentView
        // 6、发送通知
        notificationManager.notify(MUSIC_NOTIFICATION_ID, notification);
    }

    public static Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    private void replyNotification() {
        // 1、
        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY).setLabel("回复通知").build();
        // 2、可以使用广播或者Service对回复进行处理
        // Intent intent = new Intent(this, MyService.class);
        // PendingIntent pendingIntent = PendingIntent.getService(this,1,intent,PendingIntent.FLAG_CANCEL_CURRENT);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 4、
        NotificationCompat.Action action =
                new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "回复", pendingIntent).addRemoteInput(remoteInput).build();
        // 5、
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 注意：channelId 要保持一致，否则无法显示通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(REPLY_CHANNEL_ID, REPLY_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        // 5、
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, REPLY_CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("请问需要银行贷款吗?")
                .setContentText("您好，我是XX银行的XX经理， 请问你需要办理银行贷款吗？")
                .setColor(Color.CYAN)
                .setPriority(Notification.PRIORITY_MAX) // 设置优先级为Max，则为悬浮通知
                .addAction(action) // 设置回复action
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_ALL) // 想要悬浮出来， 这里必须要设置
                .setCategory(Notification.CATEGORY_MESSAGE);

        Notification notification = builder.build();
        // 6、发送通知
        notificationManager.notify(REPLY_NOTIFICATION_ID, notification);
    }

    private void downloadNotification() {
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // 注意：channelId 要保持一致，否则无法显示通知
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(DOWNLOAD_CHANNEL_ID, DOWNLOAD_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, DOWNLOAD_CHANNEL_ID);
        builder.setContentTitle("Picture Download")
                .setContentText("0%")
                .setSmallIcon(R.drawable.ic_notification)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setDefaults(Notification.DEFAULT_ALL); // 想要悬浮出来， 这里必须要设置

        // Issue the initial notification with zero progress
        int PROGRESS_MAX = 100;
        final int[] PROGRESS_CURRENT = {0};
        builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT[0], false);
        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, builder.build());
        final Handler handler = new Handler(Looper.getMainLooper());
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (PROGRESS_CURRENT[0] < 100) {
                    try {
                        Thread.sleep(1000);
                        int progress = (int) Math.floor(Math.random() * 10);
                        PROGRESS_CURRENT[0] += progress;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT[0], false)
                                        .setContentText(PROGRESS_CURRENT[0] + "%");
                                notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, builder.build());
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        builder.setContentText("Download complete")
                                .setProgress(0, 0, false);
                        notificationManager.notify(DOWNLOAD_NOTIFICATION_ID, builder.build());
                    }
                });
            }
        }).start();
    }

}
