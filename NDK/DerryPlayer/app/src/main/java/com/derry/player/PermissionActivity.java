package com.derry.player;

import android.Manifest;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionActivity extends AppCompatActivity {

  private String[] permissions = new String[]{
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.INTERNET,
  };

  public static final String KEY_PERMISSIONS = "permissions";
  public static final int RC_REQUEST_PERMISSION = 100;
  private static boolean hasRequest;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && permissions != null) {
      ActivityCompat.requestPermissions(PermissionActivity.this, permissions, RC_REQUEST_PERMISSION);
    }
  }

  @RequiresApi(api = Build.VERSION_CODES.M)
  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode != RC_REQUEST_PERMISSION) {
      return;
    }
    // 处理申请结果
//    boolean[] shouldShowRequestPermissionRationale = new boolean[permissions.length];
//    for (int i = 0; i < permissions.length; ++i) {
//      shouldShowRequestPermissionRationale[i] = shouldShowRequestPermissionRationale(permissions[i]);
//    }
//    this.onRequestPermissionsResult(permissions, grantResults, shouldShowRequestPermissionRationale);
  }

  private void onRequestPermissionsResult(String[] permissions, int[] grantResults, boolean[] shouldShowRequestPermissionRationale){

  }
}
