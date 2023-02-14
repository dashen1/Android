package com.example.thirdparty.dagger2.example2;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thirdparty.MyApplication;
import com.example.thirdparty.R;
import com.example.thirdparty.dagger2.example2.di.OtherObject;
import com.example.thirdparty.dagger2.example2.object.HttpObject;

import javax.inject.Inject;

public class Dagger2ExampleSecondActivity extends AppCompatActivity {

    @Inject
    HttpObject httpObject3;
    @Inject
    OtherObject otherObject2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2_second);

        ((MyApplication)getApplication())
                .getAppComponent()
                        .injectToSecondActivity(this);

        Log.d("Dagger2ExampleActivity", "httpObject2 : " + httpObject3.hashCode());
        Log.d("Dagger2ExampleActivity", "otherObject : " + otherObject2.hashCode());

    }
}
