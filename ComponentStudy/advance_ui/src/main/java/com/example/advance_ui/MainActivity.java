package com.example.advance_ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.example.advance_ui.transition_drawable.TransitionActivity;
import com.example.mylibrary.ActivityUtil;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_transition})
    public void onCLick(View view){
        switch (view.getId()){
            case R.id.btn_transition:
                ActivityUtil.gotoActivity(TransitionActivity.class, this);
                break;
            default:
                break;
        }
    }
}