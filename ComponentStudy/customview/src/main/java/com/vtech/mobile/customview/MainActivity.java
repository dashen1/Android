package com.vtech.mobile.customview;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.vtech.mobile.customview.activity.coordinate.CoordinateActivity;
import com.vtech.mobile.customview.taptap.TapTapActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_coordinate)
    Button btn_coordinate;

    @BindView(R.id.btn_taptap)
    Button btn_taptap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btn_coordinate.setOnClickListener(this);
        btn_taptap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_coordinate:
                gotoActivity(CoordinateActivity.class);
                break;
            case R.id.btn_taptap:
                gotoActivity(TapTapActivity.class);
                break;
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}