package com.example.componentstudy.notify;

import static com.example.componentstudy.notify.NotifyActivity.scaleBitmap;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.componentstudy.R;

public class MediaPlayerServiceTest extends Service {

    public final static int MEDIA_TEST_NOTIFICATION_ID = 10;
    public final static String MEDIA_TEST_CHANNEL_ID = "101";
    public final static String MEDIA_TEST_CHANNEL_NAME = "mediaTest";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("onCreate", "onCreate\n======================================================\n");
        setNotificationAndForeground();
    }

    private void setNotificationAndForeground(){
        //高版本的模拟器或手机还需要开启渠道才能显示通知
        // 1、自定义通知布局
        RemoteViews remoteViews;
        final String albumName = "稻香";
        final String artistName = "周杰伦";
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, "随心听");
        remoteViews.setTextViewText(R.id.text, text);
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.ic_notification);
        final Bitmap bitmap = scaleBitmap(b, 160, 160);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.image, bitmap);
        }

        // 2、Android 8.0 以上需要设置 channel
        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(MEDIA_TEST_CHANNEL_ID, MEDIA_TEST_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true); //是否在桌面icon右上角展示小红点
            notificationChannel.setLightColor(Color.YELLOW); //小红点颜色
            notificationChannel.setShowBadge(false); //是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(notificationChannel);
        }

        // 3、实例化一个意图，当点击通知时会跳转执行这个意图
        // 上一首
        Intent prevIntent = new Intent(this, MediaPlayerServiceTest.class);
        PendingIntent prevPendingIntent = PendingIntent.getActivity(this, 0, prevIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 停止播放
        Intent pauseIntent = new Intent(this, MediaPlayerServiceTest.class);
        PendingIntent pausePendingIntent = PendingIntent.getActivity(this, 0, pauseIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        // 下一首
        Intent nextIntent = new Intent(this, MediaPlayerServiceTest.class);
        PendingIntent nextPendingIntent = PendingIntent.getActivity(this, 0, nextIntent, PendingIntent.FLAG_CANCEL_CURRENT);


        RemoteViews bigContentView = new RemoteViews(getPackageName(), R.layout.notification_big);
        bigContentView.setTextViewText(R.id.text_expanded, text);
        bigContentView.setImageViewBitmap(R.id.image_expanded, bitmap);
        // 5、构建通知
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, MEDIA_TEST_CHANNEL_ID);
        builder// Show controls on lock screen even when user hides sensitive content.
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(bitmap)
                .setCustomContentView(remoteViews)
                .setCustomBigContentView(bigContentView)
//                .addAction(R.drawable.note_btn_next,"Next",nextPendingIntent)
//                .addAction(R.drawable.notification_close,"Close",pausePendingIntent)
//                .setStyle(new Notification.MediaStyle()
//                        .setShowActionsInCompactView(1 /* #1: pause button */)
//                        .setMediaSession(mediaSession.getSessionToken()))
                .setContentTitle("Wonderful music")
                .setContentText("My Awesome Band")
                .setLargeIcon(bitmap)
                .build();
        Notification notification = builder.build();
        startForeground(MEDIA_TEST_NOTIFICATION_ID,notification);
    }
}
