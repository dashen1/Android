package com.example.thirdparty.okhttp;

import android.Manifest;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.example.thirdparty.R;
import com.example.thirdparty.okhttp.cacheinterceptor.CacheResponseInterceptor;
import com.example.thirdparty.okhttp.cacheinterceptor.InternetCacheInterceptor;
import com.permissionx.guolindev.PermissionX;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

@Route(path = OkHttpActivity.PATH)
public class OkHttpActivity extends AppCompatActivity {

    private static final String TAG = "OkHttpActivity";

    public static final String PATH = "/okhttp/OkHttpActivity";

//    @BindView(R.id.btn_download)
//    public Button btn_download;

    @OnClick(R.id.btn_download)
    void upLoadFile() {
        // okhttp 上传文件
        String url = "http://api.yesapi.cn/api/App/CDN/UploadImg";
        File file = new File("/sdcard/two.jpg");
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        builder.addFormDataPart("platform", "android");
        builder.addFormDataPart("file", file.getName(),
                RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
        // 构建请求
        ExMultipartBody exMultipartBody = new ExMultipartBody(builder.build(), new UploadProgressListener() {
            @Override
            public void onProgress(long total, long current) {
                Log.i(TAG, total + " : " + current);
            }
        });
        final Request request = new Request.Builder()
                .url(url)
                .post(exMultipartBody)
                .build();
        // 添加日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"onFailure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.i(TAG,"onResponse");
            }
        });
    }

    @OnClick(R.id.btn_download_cache)
    void upLoadFileCache(){
        //自定义缓存（要求：有望 30s内请求读缓存，无网直接都缓存）
        //okhttp自带的扩展有坑
        //利用okhttp拦截器
        String url = "https://www.baidu.com";
        final Request request = new Request.Builder()
                .url(url)
                .build();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        File cacheFile = new File("/sdcard/cache");
        Cache cache = new Cache(cacheFile, 10 * 1024 * 1024);

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .cache(cache)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new InternetCacheInterceptor(this))
                .addNetworkInterceptor(new CacheResponseInterceptor())
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i(TAG,"onFailure");
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                Log.e(TAG,"response.body : "+response.body().string());
                Log.e(TAG,response.cacheResponse()+" : "+response.networkResponse());
            }
        });
    }

    private String guessMimeType(String filePath) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String mimeType = fileNameMap.getContentTypeFor(filePath);
        if (TextUtils.isEmpty(mimeType)) {
            return "application/octet-stream";
        }
        return mimeType;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        ARouter.getInstance().inject(this);
        ButterKnife.bind(this);

        PermissionX.init(OkHttpActivity.this)
                .permissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request((allGranted, grantedList, deniedList) -> {
                    if (allGranted) {
                        Toast.makeText(this, "All permissions are granted!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "No permissions are granted! Please granted the permissions!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}
