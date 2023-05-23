package com.vtech.mobile.tinker.app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tencent.tinker.lib.tinker.TinkerInstaller;
import com.vtech.mobile.tinker.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button load;
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        load = findViewById(R.id.load);
        test = findViewById(R.id.test);
        load.setOnClickListener(this);
        test.setOnClickListener(this);
        requestPermission();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.load:
                // 加载补丁
                TinkerInstaller.onReceiveUpgradePatch(getApplicationContext(),
                        Environment.getExternalStorageDirectory().getAbsolutePath() + "/tinker-debug-patch_signed.apk");
                break;
            case R.id.test:
                Toast.makeText(MainActivity.this,"bug 修复 ",Toast.LENGTH_LONG).show();
                break;
        }
    }

    // 请求读文件权限
    private void requestPermission() {
        if (!checkPermission()) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    // 检查是否有读文件权限
    private boolean checkPermission() {
        final int res = ContextCompat.checkSelfPermission(this.getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return res == PackageManager.PERMISSION_GRANTED;
    }
}