package com.example.thirdparty.arouter;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.launcher.ARouter;
import com.example.thirdparty.R;
import com.example.thirdparty.okhttp.OkHttpActivity;
import com.example.thirdparty.retrofit.RetrofitActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArouterActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.btn_goto_okhttp)
    public Button btn_goto_okhttp;

    @BindView(R.id.btn_goto_retrofit)
    public Button btn_goto_retrofit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arouter);
        ButterKnife.bind(this);
        btn_goto_okhttp.setOnClickListener(this);
        btn_goto_retrofit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_goto_okhttp:
                ARouter.getInstance()
                        .build(OkHttpActivity.PATH)
                        .navigation();
                break;
            case R.id.btn_goto_retrofit:
                ARouter.getInstance()
                        .build(RetrofitActivity.PATH)
                        .navigation();
                break;
        }
    }
}
