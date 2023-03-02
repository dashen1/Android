package com.example.demo;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.demo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding dataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        String string = "";
        try {

            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            string = bundle.getString("string");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        dataBinding.setMCount(0);
        String finalString = string;
        dataBinding.btnClick.setOnClickListener(v -> {
            dataBinding.setMCount(getRandomInRange(49));
            dataBinding.setMValue(finalString);
        });


    }

    public static int getRandomInRange(int range) {
        return (int) (1 + Math.random() * (range));
    }

}