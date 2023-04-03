package com.vtech.mobile.kidiconnect2021;

import android.os.Bundle;

import com.vtech.mobile.kidiconnect2021.customcamera.view.CustomSurfaceView;

public class MainActivity extends PermissionActivity {

    private CustomSurfaceView customSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        customSurfaceView = findViewById(R.id.custom_surface_view);

        customSurfaceView.setActivity(this);
    }
}