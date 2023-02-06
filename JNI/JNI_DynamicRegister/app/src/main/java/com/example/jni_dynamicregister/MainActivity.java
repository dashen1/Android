package com.example.jni_dynamicregister;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.jni_dynamicregister.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'jni_dynamicregister' library on application startup.
    static {
        System.loadLibrary("jni_dynamicregister");//默认调用 JNI_OnLoad
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //静态注册 new 出来

        //动态注册 s = new () 所有的初始化已做
    }

    public void updateActivityUI() {

    }

    public native String stringFromJNI();

    public native void staticRegister();

    public native void dynamicJavaMethod01();
    public native int dynamicJavaMethod02(String str);

    public native void nativeThread();

    public native void nativeFun1();
    public native void nativeFun2();
    public native void nativeFun3();
    public native void nativeFun4();

}