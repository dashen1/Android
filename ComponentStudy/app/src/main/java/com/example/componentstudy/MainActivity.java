package com.example.componentstudy;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.aidl_service.AIDLActivity;
import com.example.componentstudy.broadcast.BroadcastActivity;
import com.example.componentstudy.contentprovider.ContentProviderActivity;
import com.example.componentstudy.lifecycle.LifecycleActivity;
import com.example.componentstudy.service.ServiceActivity;
import com.example.componentstudy.sqlite.SQLiteActivity;
import com.example.componentstudy.viewmodule.ViewModuleActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btn_sqlite;
    private Button btn_broadcast;
    private Button btn_service;
    private Button btn_contentprovider;
    private Button btn_aidl_service;
    private Button btn_lifecycle;
    private Button btn_viewmodule;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sqlite = findViewById(R.id.btn_sqlite);
        btn_broadcast = findViewById(R.id.btn_broadcast);
        btn_service = findViewById(R.id.btn_service);
        btn_contentprovider = findViewById(R.id.btn_contentprovider);
        btn_aidl_service = findViewById(R.id.btn_aidl_service);
        btn_lifecycle = findViewById(R.id.btn_lifecycle);
        btn_viewmodule = findViewById(R.id.btn_viewmodule);

        btn_sqlite.setOnClickListener(this);
        btn_broadcast.setOnClickListener(this);
        btn_service.setOnClickListener(this);
        btn_contentprovider.setOnClickListener(this);
        btn_aidl_service.setOnClickListener(this);
        btn_lifecycle.setOnClickListener(this);
        btn_viewmodule.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sqlite:
                gotoActivity(SQLiteActivity.class);
                break;
            case R.id.btn_broadcast:
                gotoActivity(BroadcastActivity.class);
                break;
            case R.id.btn_service:
                gotoActivity(ServiceActivity.class);
                break;
            case R.id.btn_contentprovider:
                gotoActivity(ContentProviderActivity.class);
                break;
            case R.id.btn_aidl_service:
                gotoActivity(AIDLActivity.class);
                break;
            case R.id.btn_lifecycle:
                gotoActivity(LifecycleActivity.class);
                break;
            case R.id.btn_viewmodule:
                gotoActivity(ViewModuleActivity.class);
                break;
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
