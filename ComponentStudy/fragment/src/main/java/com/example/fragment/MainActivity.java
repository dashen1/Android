package com.example.fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.fragment.fragment.BackStackActivity;
import com.example.fragment.fragment.BlankFragment;
import com.example.fragment.fragment.BlankFragment2;
import com.example.fragment.navigator.NavigatorActivity;
import com.example.fragment.tradition.TraditionActivity;
import com.example.fragment.viewpager.ViewPagerActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btn_tradition;
    private Button btn_navigation;
    private Button btn_viewpager;
    private Button btn_back_stack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_tradition = findViewById(R.id.btn_tradition);
        btn_tradition.setOnClickListener(this);

        btn_navigation = findViewById(R.id.btn_navigation);
        btn_navigation.setOnClickListener(this);

        btn_viewpager = findViewById(R.id.btn_viewpager);
        btn_viewpager.setOnClickListener(this);

        btn_back_stack = findViewById(R.id.btn_back_stack);
        btn_back_stack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_tradition:
                gotoActivity(TraditionActivity.class);
                break;
            case R.id.btn_navigation:
                gotoActivity(NavigatorActivity.class);
                break;
            case R.id.btn_viewpager:
                gotoActivity(ViewPagerActivity.class);
                break;
            case R.id.btn_back_stack:
                gotoActivity(BackStackActivity.class);
                break;
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

}