package com.example.opengl;

import android.os.Bundle;

import com.example.opengl.databinding.ActivityMainBinding;

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