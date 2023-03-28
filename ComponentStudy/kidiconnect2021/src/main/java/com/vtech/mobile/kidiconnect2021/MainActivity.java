package com.vtech.mobile.kidiconnect2021;

import android.os.Bundle;

import com.vtech.mobile.kidiconnect2021.databinding.ActivityMainBinding;

public class MainActivity extends PermissionActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.customSurfaceView.setActivity(MainActivity.this);
    }
}