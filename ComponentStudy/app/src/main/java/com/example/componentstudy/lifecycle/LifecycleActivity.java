package com.example.componentstudy.lifecycle;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.example.componentstudy.R;

public class LifecycleActivity extends AppCompatActivity implements LifecycleOwner {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lifecycle);

        //getLifecycle().addObserver(new LifecycleUtil());
    }

    public void clearActivity(View view) {
        MyActivityLifeCycleCallBack.getInstance().clearAllActivity();
    }
}
