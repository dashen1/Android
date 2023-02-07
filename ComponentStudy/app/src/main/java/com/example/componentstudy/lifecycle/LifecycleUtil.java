package com.example.componentstudy.lifecycle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.componentstudy.MyApplication;

public class LifecycleUtil implements DefaultLifecycleObserver {

    private static final String TAG = "LifecycleUtil";

    @Override
    public void onCreate(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onCreate(owner);
        Log.i(TAG, "ON_START");
        Toast.makeText(MyApplication.getContext(), "ON_START", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStop(@NonNull LifecycleOwner owner) {
        DefaultLifecycleObserver.super.onStop(owner);
        Log.i(TAG, "ON_STOP");
        Toast.makeText(MyApplication.getContext(), "ON_STOP", Toast.LENGTH_SHORT).show();
    }
}
