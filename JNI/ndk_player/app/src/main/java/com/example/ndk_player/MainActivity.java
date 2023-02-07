package com.example.ndk_player;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ndk_player.databinding.ActivityMainBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private DerryPlayer player;
    private TextView tv_state;

    private SurfaceView surfaceView;

    /*权限请求Code*/
    private final static int PERMISSION_REQUEST_CODE = 1234;
    /*我们需要使用的权限*/
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        tv_state = findViewById(R.id.tv_state);
        checkPermission();
        surfaceView = findViewById(R.id.surfaceView);
        player = new DerryPlayer();
        player.setSurfaceView(surfaceView);
        File file = new File("/sdcard" + File.separator + "demo.mp4");
        player.setDataSource(file.getAbsolutePath());
        Log.d(TAG, file.getAbsolutePath());
        // 准备成功调用处
        player.setOnPreparedListener(new DerryPlayer.OnPreparedListener() {
            @Override
            public void onPrepared() {
                //这里是c++调用回来的 需要另开线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this, "准备成功，即将播放!", Toast.LENGTH_SHORT).show();
                    }
                });
                player.start();
            }
        });

        player.setOnErrorListener(new DerryPlayer.OnErrorListener() {
            @Override
            public void onError(String errorCode) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_state.setTextColor(Color.GREEN);
                        tv_state.setText("初始化失败！");
                    }
                });
            }
        });
    }


    @Override
    protected void onResume() { // Activity.java Handler
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

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                checkSelfPermission(permissions[0]) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(permissions[1]) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(permissions[2]) != PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(permissions[3]) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(permissions, PERMISSION_REQUEST_CODE);
        }
    }

    //权限反馈
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                /*PackageManager.PERMISSION_GRANTED  权限被许可*/
                /*PackageManager.PERMISSION_DENIED  没有权限；拒绝访问*/
                if (grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    showWaringDialog("无法读取内存卡！");
                } else if (grantResults.length > 0 && grantResults[1] != PackageManager.PERMISSION_GRANTED) {
                    showWaringDialog("无法读取内存卡！");
                } else if (grantResults.length > 0 && grantResults[2] != PackageManager.PERMISSION_GRANTED) {
                    showWaringDialog("无法使用相机！");
                } else if (grantResults.length > 0 && grantResults[3] != PackageManager.PERMISSION_GRANTED) {
                    showWaringDialog("无法录制音频！");
                }
                break;
        }
    }

    private void showWaringDialog(String msg) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("警告！")
                .setMessage(msg)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，我们暂时做退出处理
                        finish();
                    }
                }).setPositiveButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 一般情况下如果用户不授权的话，功能是无法运行的，我们暂时做退出处理
                        finish();
                    }
                }).show();
    }
}