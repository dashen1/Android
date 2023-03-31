package com.vtech.mobile.kidiconnect2021;

import android.os.Bundle;

import com.vtech.mobile.kidiconnect2021.customcamera.widget.CameraSurfaceView;

public class MainActivity extends PermissionActivity {

    private CameraSurfaceView cameraSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView( R.layout.activity_main);
        cameraSurfaceView = findViewById(R.id.custom_surface_view);
        cameraSurfaceView.setActivity(MainActivity.this);
    }
}