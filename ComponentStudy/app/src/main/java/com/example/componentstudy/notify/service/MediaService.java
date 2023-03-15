package com.example.componentstudy.notify.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.componentstudy.MediaAidlInterface;
import com.example.componentstudy.R;
import com.example.componentstudy.util.ImageUtils;

import java.lang.ref.WeakReference;

public class MediaService extends Service {

    public static final String TOGGLEPAUSE_ACTION = "com.wm.remusic.togglepause";

    public static final String STOP_ACTION = "com.wm.remusic.stop";

    private boolean isPlaying = false;
    private final IBinder mBinder = new ServiceStub(this);

    private NotificationManager mNotificationManager;
    private Notification mNotification;

    private static final int NOTIFY_MODE_NONE = 0;
    private static final int NOTIFY_MODE_FOREGROUND = 1;
    private static final int NOTIFY_MODE_BACKGROUND = 2;
    private int mNotificationId = 1000;
    private long mNotificationPostTime = 0;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MediaService","onCreate");
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        updateNotification();
    }

    private void updateNotification(){
        final int newNotifyMode = NOTIFY_MODE_FOREGROUND;;
        Log.d("MediaService","updateNotification");
        mNotificationManager.notify(mNotificationId, getNotification());
    }

    private Notification getNotification() {
        RemoteViews remoteViews;
        final int PAUSE_FLAG = 0x1;
        final int STOP_FLAG = 0x2;
        final String albumName = "七里香";
        final String artistName = "周杰伦";
        final boolean isPlaying = isPlaying();

        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, "随心听");
        remoteViews.setTextViewText(R.id.text, text);

        // 此处的action不能是一样的 如果一样 接受的flag参数只是第一个设置的值
        Intent pauseIntent = new Intent(TOGGLEPAUSE_ACTION);
        pauseIntent.putExtra("FLAG",PAUSE_FLAG);
        PendingIntent pausePIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, 0);
        remoteViews.setImageViewResource(R.id.iv_pause,isPlaying?R.drawable.note_btn_pause:R.drawable.note_btn_play);
        remoteViews.setOnClickPendingIntent(R.id.iv_pause,pausePIntent);

        final Intent nowPlayingIntent = new Intent();
        nowPlayingIntent.setComponent(new ComponentName("com.example.componentstudy","com.example.componentstudy.notify.NotifyActivity"));
        nowPlayingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent click = PendingIntent.getActivity(this, 0, nowPlayingIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        final Bitmap bitmap = ImageUtils.getArtworkQuick(this,R.drawable.funny,160,160);
        if (bitmap!=null){
            remoteViews.setImageViewBitmap(R.id.image,bitmap);
        }
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }

        if (mNotification==null){
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this).setContent(remoteViews)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentIntent(click)
                    .setWhen(mNotificationPostTime);
            mNotification = builder.build();
        }else {
            mNotification.contentView = remoteViews;
        }
        return mNotification;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private static final class ServiceStub extends MediaAidlInterface.Stub {

        private final WeakReference<MediaService> mService;

        public ServiceStub(final MediaService service) {
            this.mService = new WeakReference<>(service);
        }

        @Override
        public void stop() throws RemoteException {
            mService.get().setPlaying(false);
        }

        @Override
        public void pause() throws RemoteException {
            mService.get().setPlaying(false);
        }

        @Override
        public void play() throws RemoteException {
            mService.get().setPlaying(true);
        }

        @Override
        public boolean isPlaying() throws RemoteException {
            return mService.get().isPlaying();
        }
    }
}
