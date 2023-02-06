package com.example.bboyplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.graphics.Color;
import android.os.Bundle;
import android.view.SurfaceView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends PermissionActivity {


  private BboyPlayer player;
  private TextView tv_state;
  private SurfaceView surfaceView;

  private SeekBar seekBar;
  private TextView tv_time;
  private boolean isTouch;
  private int duration;

  private WebView webView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    tv_state = findViewById(R.id.tv_state);
    surfaceView = findViewById(R.id.surfaceView);


    player = new BboyPlayer();
    player.setSurfaceView(surfaceView);
    player.setDataSource("/sdcard/demo.mp4");
    player.setOnPreparedListener(new BboyPlayer.OnPreparedListener() {
      @Override
      public void onPrepared() {
        //duration = player.getDuration();
        //因为此时的onPrepared方法是在子线程中调用的 即子线程调用主线程
        runOnUiThread(new Runnable() {
          @Override
          public void run() {
//            if(duration!=0){
//            }
            tv_state.setTextColor(Color.RED);
            tv_state.setText("恭喜init初始化成功！");
          }
        });
        player.start();
      }
    });

    player.setOnErrorListener(new BboyPlayer.OnErrorListener() {
      @Override
      public void onError(String errorInfo) {
          runOnUiThread(new Runnable() {
            @Override
            public void run() {
              tv_state.setTextColor(Color.RED);
              tv_state.setText("视频发生了错误："+errorInfo);
            }
          });
      }
    });

    player.setOnProgressListener(new BboyPlayer.OnProgressListener() {
      @Override
      public void onProgress(int progress) {

      }
    });
  }


  @Override
  protected void onResume() {
    super.onResume();
    player.prepare();
  }

  @Override
  protected void onStop() {
    super.onStop();
    player.stop();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    player.release();
  }


  public native String stringFromJNI();
}
