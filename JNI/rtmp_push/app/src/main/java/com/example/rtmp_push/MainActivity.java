package com.example.rtmp_push;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.example.rtmp_push.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

   private static final String TAG = "MainActivity";

   // private CameraHelper mCameraHelper;
    private DerryPush pusher;

    /*权限请求Code*/
    private final static int PERMISSION_REQUEST_CODE = 1234;
    /*我们需要使用的权限*/
    private String[] permissions = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        checkPermission();
        SurfaceView surfaceView = findViewById(R.id.surfaceView);
        pusher = new DerryPush(MainActivity.this, 640, 480, Camera.CameraInfo.CAMERA_FACING_BACK, 25, 80000);
        pusher.setPreviewDisplay(surfaceView.getHolder());
    }

    public native String stringFromJNI();

    /**
     * 切换摄像头
     * @param view
     */
    public void switchCamera(View view) {
        pusher.switchCamera();
    }

    /**
     * 开始直播
     * @param view
     */
    public void startLive(View view) {
        pusher.startLive();
    }

    /**
     * 停止直播
     * @param view
     */
    public void stopLive(View view) {
        pusher.stopLive();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        pusher.release();
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