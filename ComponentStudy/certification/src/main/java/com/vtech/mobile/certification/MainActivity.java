package com.vtech.mobile.certification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String BAIDU_URL = "https://www.baidu.com/";
    private String TOMCAT_URL = "https://localhost:8089/";
    private static OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mOkHttpClient = createSSLClient(createTrustAllTrustManager());
    }

    /**
     * 创建新人所有证书的TrustManager
     * @return
     */
    private static X509TrustManager createTrustAllTrustManager(){
        return new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d(TAG, "checkClientTrusted");
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                Log.d(TAG, "checkServerTrusted");
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                Log.d(TAG, "getAcceptedIssuers");
                return new X509Certificate[0];
            }
        };
    }

    private static class TrustAllHostnameVerifier implements HostnameVerifier{

        @Override
        public boolean verify(String hostname, SSLSession session) {
            // 域名校验，默认都通过
            Log.d(TAG, "verify");
            return true;
        }
    }

    // 根据自定义X509TrustManager创建OkHttpSSLClient
    private static  OkHttpClient createSSLClient(X509TrustManager x509TrustManager){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .sslSocketFactory(createSSLSocketFactory(x509TrustManager), x509TrustManager)
                .hostnameVerifier(new TrustAllHostnameVerifier());
        Log.d(TAG,"createSSLClient");
        return builder.build();
    }

    private static SSLSocketFactory createSSLSocketFactory(TrustManager trustManager){
        SSLSocketFactory sslSocketFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{trustManager},new SecureRandom());
            sslSocketFactory = sc.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }

    public void getBaidu(View view) {
        Request request = new Request.Builder()
                .url(BAIDU_URL)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "getBaidu onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "getBaidu response: " + response.body().string());
            }
        });
    }

    public void getTomcat(View view) {
        Request request = new Request.Builder()
                .url(TOMCAT_URL)
                .build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("TAG", "getTomcat onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i("TAG", "getTomcat response: " + response.body().string());
            }
        });
    }
}