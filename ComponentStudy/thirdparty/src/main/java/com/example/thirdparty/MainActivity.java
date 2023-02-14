package com.example.thirdparty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.thirdparty.arouter.ArouterActivity;
import com.example.thirdparty.dagger2.example2.Dagger2ExampleActivity;
import com.example.thirdparty.okhttp.OkHttpActivity;
import com.example.thirdparty.retrofit.RetrofitActivity;
import com.example.thirdparty.rxjava.sample1.RxJavaActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    @BindView(R.id.btn_okhttp)
    public Button btn_okhttp;

    @BindView(R.id.btn_retrofit)
    public Button btn_retrofit;

    @BindView(R.id.btn_rxjava)
    public Button btn_rxjava;

    @BindView(R.id.btn_arouter)
    public Button btn_arouter;

    @BindView(R.id.btn_dagger2)
    public Button btn_dagger2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        btn_okhttp.setOnClickListener(this);
        btn_retrofit.setOnClickListener(this);
        btn_rxjava.setOnClickListener(this);
        btn_arouter.setOnClickListener(this);
        btn_dagger2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_okhttp:
                gotoActivity(OkHttpActivity.class);
                break;
            case R.id.btn_retrofit:
                gotoActivity(RetrofitActivity.class);
                break;
            case R.id.btn_rxjava:
                gotoActivity(RxJavaActivity.class);
                break;
            case R.id.btn_arouter:
                gotoActivity(ArouterActivity.class);
                break;
            case R.id.btn_dagger2:
                gotoActivity(Dagger2ExampleActivity.class);
                break;
        }
    }

    private void gotoActivity(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }
}