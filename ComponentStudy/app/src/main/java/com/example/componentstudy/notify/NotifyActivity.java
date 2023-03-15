package com.example.componentstudy.notify;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.componentstudy.R;
import com.example.componentstudy.lifecycle.LifecycleActivity;
import com.example.componentstudy.notify.service.MediaPlayer;
import com.example.componentstudy.util.ImageUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NotifyActivity extends AppCompatActivity {

    @BindView(R.id.btn_play_or_pause)
    Button btn_play_or_pause;
    @BindView(R.id.btn_test)
    Button btn_test;
    int clickCount = 0;

    private int notificationId = 2000;

    private String CHANNEL_ID = "1001";
    private Notification notification;
    private long mNotificationPostTime = 0;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify);
        ButterKnife.bind(this);

        btn_play_or_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount % 2 == 0) {
                    // start
                    MediaPlayer.playOrPause(true);
                } else {
                    // stop
                    MediaPlayer.playOrPause(false);
                }
                clickCount++;
            }
        });

        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNotificationChannel();
            }
        });
    }
    


    private void createNotificationChannel() {
        //高版本的模拟器或手机还需要开启渠道才能显示通知
        RemoteViews remoteViews;
        final String albumName = "七里香";
        final String artistName = "周杰伦";
        remoteViews = new RemoteViews(this.getPackageName(), R.layout.notification);
        String text = TextUtils.isEmpty(albumName) ? artistName : artistName + " - " + albumName;
        remoteViews.setTextViewText(R.id.title, "随心听");
        remoteViews.setTextViewText(R.id.text, text);

        NotificationChannel notificationChannel = null;
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel("001", "channel_name", NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "001");
        //实例化一个意图，当点击通知时会跳转执行这个意图
        Intent intent = new Intent(this, LifecycleActivity.class);
        //将意图进行封装
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        //设置Notification的点击之后执行的意图
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.drawable.funny);
        final Bitmap bitmap = scaleBitmap(b, 160, 160);
        // final Bitmap bitmap = ImageUtils.getArtworkQuick(this, R.drawable.funny, 160, 160);
        if (bitmap != null) {
            remoteViews.setImageViewBitmap(R.id.image, bitmap);
        }
        if (mNotificationPostTime == 0) {
            mNotificationPostTime = System.currentTimeMillis();
        }
        builder.setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setWhen(mNotificationPostTime);
        notification = builder.build();
        notification.contentView = remoteViews;
        notificationManager.notify(0, notification);
    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
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
}
