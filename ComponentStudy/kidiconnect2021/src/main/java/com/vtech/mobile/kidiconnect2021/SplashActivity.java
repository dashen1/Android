package com.vtech.mobile.kidiconnect2021;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class SplashActivity extends PermissionActivity{

    private String TAG = "SplashActivity";

    private String[] permission = {
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        RxPermissions rxPermissions = new RxPermissions(SplashActivity.this);
        rxPermissions.request(permission)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean){
                            navigateToMainScreen();
                        }else {
                            Log.d(TAG,"Granted failed!");
                        }
                    }
                });
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permissionCheck = 0;
            permissionCheck = this.checkSelfPermission(Manifest.permission.CAMERA);       //允许一个程序访问精良位置(如GPS)
            permissionCheck += this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);    //允许一个程序访问CellID或WiFi热点来获取粗略的位置
            permissionCheck += this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);                    //请求访问使用照相设备

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {         //未获得权限
                this.requestPermissions( // 请求授权
                        permission,100);// 自定义常量,任意整型
            }
        }
    }


//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if(requestCode == 100){
//            if (hasAllPermissionGranted(grantResults)) {
//                Log.i(TAG, "onRequestPermissionsResult: 用户允许权限");
//                navigateToMainScreen();
//            } else {
//                Log.i(TAG, "onRequestPermissionsResult: 拒绝搜索设备权限");
//            }
//        }
//    }

    private boolean hasAllPermissionGranted(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }

    private void navigateToMainScreen() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
