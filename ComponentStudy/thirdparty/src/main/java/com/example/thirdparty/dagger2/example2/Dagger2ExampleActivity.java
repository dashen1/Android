package com.example.thirdparty.dagger2.example2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thirdparty.MyApplication;
import com.example.thirdparty.R;

import com.example.thirdparty.dagger2.example2.di.OtherObject;
import com.example.thirdparty.dagger2.example2.object.HttpObject;

import javax.inject.Inject;

public class Dagger2ExampleActivity extends AppCompatActivity {

    @Inject
    HttpObject httpObject1;
    @Inject
    HttpObject httpObject2;

    @Inject
    OtherObject otherObject;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dagger2);
//        DaggerMyComponent.builder()
//                .httpModule(new HttpModule())
//                .databaseModule(new DatabaseModule())
//                .mySecondComponent(DaggerMySecondComponent.create())
//                .build()
//                .injectToMainActivity(this);

        ((MyApplication)getApplication())
                .getAppComponent()
                        .injectToMainActivity(this);
        Log.d("Dagger2ExampleActivity", "httpObject1 : " + httpObject1.hashCode());
        Log.d("Dagger2ExampleActivity", "httpObject2 : " + httpObject2.hashCode());
        Log.d("Dagger2ExampleActivity", "otherObject : " + otherObject.hashCode());
    }

    public void click(View view) {
        startActivity(new Intent(this,Dagger2ExampleSecondActivity.class));
    }
}
