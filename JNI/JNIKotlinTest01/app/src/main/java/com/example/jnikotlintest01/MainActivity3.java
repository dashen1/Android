package com.example.jnikotlintest01;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity3 extends AppCompatActivity {

    public static final String TAG = "MainActivity3";
    static {
        System.loadLibrary("jnikotlintest01");
    }

    static String name1 = "T1";

    public static native void exception();
    public static native void exception2() throws NoSuchFieldException;// NoSuchFieldException接收c++层抛来的异常
    public static native void exception3();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void exceptionAction() {
        exception();
        //捕获c++抛来的异常

        try {
            exception2();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        exception3();
    }

    public static void show() throws NoSuchFieldException {
        Log.d(TAG, "操作...");
        Log.d(TAG, "操作...");
        Log.d(TAG, "操作...");

        throw new NoSuchFieldException();
    }


}
