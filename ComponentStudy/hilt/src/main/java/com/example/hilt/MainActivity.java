package com.example.hilt;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.hilt.base.HttpObject;
import com.example.hilt.base.interfacedi.TestInterface;
import com.example.hilt.proxy.bean.ResponseData;
import com.example.hilt.proxy.httpprocessor.HttpCallback;
import com.example.hilt.proxy.httpprocessor.HttpHelperProxy;
import com.example.hilt.proxy.httpprocessor.IHttpProcessor;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity {

    @Inject
    HttpObject httpObject;
    @Inject
    HttpObject httpObject2;

    @Inject
    TestInterface testInterface;

    private IHttpProcessor iHttpProcessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("MainActivity",httpObject.hashCode()+"");
        Log.d("MainActivity",httpObject2.hashCode()+"");
        testInterface.method();
        iHttpProcessor = ((MyApplication)getApplication()).getHttpProcessor();

    }

    public void click(View view) {
        String url = "https://www.wanandroid.com/user/login?username=code123&password=code123";
        HashMap<String, Object> params = new HashMap<>();
//        params.put("username","code123");
//        params.put("password","code123");
        HttpHelperProxy.obtain().post(url, params, new HttpCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData objResult) {
                Toast.makeText(MainActivity.this, objResult.getData().getNickname(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void clickWithHilt(View view) {
        String url = "https://www.wanandroid.com/user/login";
        HashMap<String, Object> params = new HashMap<>();
        params.put("username","code123");
        params.put("password","code123");
        iHttpProcessor.post(url, params, new HttpCallback<ResponseData>() {
            @Override
            public void onSuccess(ResponseData objResult) {
                Toast.makeText(MainActivity.this, objResult.getData().getNickname(), Toast.LENGTH_LONG).show();
            }
        });
    }
}