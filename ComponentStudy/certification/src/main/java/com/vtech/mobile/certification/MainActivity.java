package com.vtech.mobile.certification;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private String BAIDU_URL = "https://www.baidu.com/";
    private String TOMCAT_URL = "https://192.168.1.106:8181/";
    private static OkHttpClient mOkHttpClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 支持所有证书，包括系统证书和任何自签名证书
        // mOkHttpClient = createSSLClient(createTrustAllTrustManager());
        // 支持自签名证书但不支持系统证书
        // mOkHttpClient = createSSLClient(createTrustCustomTrustManager(getInputStreamFromAsset()));
        // 支持自签名证书和系统证书
        mOkHttpClient = createSSLClient(createTrustCustomAndDefaultTrustManager(getInputStreamFromAsset()));
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

    private InputStream getInputStreamFromAsset() {
        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("my_client.cer");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    /**
     * 创建既信任自签名证书又信任系统自带证书的TrustManager
     */
    private static X509TrustManager createTrustCustomAndDefaultTrustManager(InputStream inputStream) {
        try {
            // 获取信任系统自带证书的TrustManager
            final X509TrustManager systemTrustManager = getSystemTrustManager();
            // 获取信任自签名证书的TrustManager
            final X509TrustManager selfTrustManager = createTrustCustomTrustManager(inputStream);

            return new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    systemTrustManager.checkClientTrusted(chain, authType);
                }
                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    try {
                        // 默认使用信任自签名证书的TrustManager验证服务端身份
                        selfTrustManager.checkServerTrusted(chain, authType);
                    } catch (CertificateException e) {
                        // 此处使用系统自带SSL证书验证服务端身份
                        systemTrustManager.checkServerTrusted(chain, authType);
                    }
                }
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return systemTrustManager.getAcceptedIssuers();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建只信任指定证书的TrustManager
     * @param inputStream：证书输入流
     * @return
     */
    @Nullable
    private static X509TrustManager createTrustCustomTrustManager(InputStream inputStream) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);

            Certificate certificate = certificateFactory.generateCertificate(inputStream);
            //将证书放入keystore中
            String certificateAlias = "ca";
            keyStore.setCertificateEntry(certificateAlias, certificate);
            if (inputStream != null) {
                inputStream.close();
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.
                    getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:"
                        + Arrays.toString(trustManagers));
            }
            return (X509TrustManager) trustManagers[0];
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 创建信任系统自带证书的TrustManager
     */
    private static X509TrustManager getSystemTrustManager() throws NoSuchAlgorithmException, KeyStoreException {
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());

        tmf.init((KeyStore) null);
        for (TrustManager tm : tmf.getTrustManagers()) {
            if (tm instanceof X509TrustManager) {
                return (X509TrustManager) tm;
            }
        }
        return null;
    }
}