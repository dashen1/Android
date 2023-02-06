package com.example.componentstudy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.componentstudy.aidl_service.AIDLActivity;
import com.example.componentstudy.broadcast.BroadcastActivity;
import com.example.componentstudy.contentprovider.ContentProviderActivity;
import com.example.componentstudy.service.ServiceActivity;
import com.example.componentstudy.sqlite.SQLiteActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private Button btn_sqlite;
    private Button btn_broadcast;
    private Button btn_service;
    private Button btn_contentprovider;
    private Button btn_aidl_service;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_sqlite = findViewById(R.id.btn_sqlite);
        btn_broadcast = findViewById(R.id.btn_broadcast);
        btn_service = findViewById(R.id.btn_service);
        btn_contentprovider = findViewById(R.id.btn_contentprovider);
        btn_aidl_service = findViewById(R.id.btn_aidl_service);

        btn_sqlite.setOnClickListener(this);
        btn_broadcast.setOnClickListener(this);
        btn_service.setOnClickListener(this);
        btn_contentprovider.setOnClickListener(this);
        btn_aidl_service.setOnClickListener(this);
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
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}
