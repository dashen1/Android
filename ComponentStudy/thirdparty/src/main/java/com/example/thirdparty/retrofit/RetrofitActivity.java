package com.example.thirdparty.retrofit;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.thirdparty.R;
import com.example.thirdparty.retrofit.sample1.RetrofitClientSample1;
import com.example.thirdparty.retrofit.sample1.UserLoginResult;

import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Route(path = RetrofitActivity.PATH)
public class RetrofitActivity extends AppCompatActivity {

    private static final String TAG = "RetrofitActivity";
    public static final String PATH = "/okhttp/RetrofitActivity";


    @OnClick(R.id.btn_register)
    void register(){
        RetrofitClientSample1.getServiceApi().userRegister("coder123","coder123","coder123")
                .enqueue(new Callback<UserLoginResult>() {
                    @Override
                    public void onResponse(Call<UserLoginResult> call, Response<UserLoginResult> response) {
                        Log.i(TAG,"register success!");
                    }

                    @Override
                    public void onFailure(Call<UserLoginResult> call, Throwable t) {
                        Log.i(TAG,"register failed!");
                    }
                });
    }

    @OnClick(R.id.btn_login)
    void login(){
        RetrofitClientSample1.getServiceApi().userLogin("coder123","coder123")
                .enqueue(new Callback<UserLoginResult>() {
                    @Override
                    public void onResponse(Call<UserLoginResult> call, Response<UserLoginResult> response) {
                        Log.i(TAG,"login success!");
                    }

                    @Override
                    public void onFailure(Call<UserLoginResult> call, Throwable t) {
                        Log.i(TAG,"login failed!");
                    }
                });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retrofit);
        ButterKnife.bind(this);
        ARouter.getInstance().inject(this);

    }
}
