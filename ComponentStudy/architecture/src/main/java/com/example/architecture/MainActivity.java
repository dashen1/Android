package com.example.architecture;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.architecture.mvvm.MVVMActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_mvvm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_mvvm = findViewById(R.id.btn_mvvm);

        btn_mvvm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_mvvm:
                gotoActivity(MVVMActivity.class);
                break;
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}