package com.example.jnikotlintest01;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    static {
        System.loadLibrary("jnikotlintest01");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public static native void loadCache(String name);
    public static native void initStaticCache();
    public static native void clearStaticCache();
}
