package com.example.thirdparty.okhttp.cacheinterceptor;

import android.content.Context;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class InternetCacheInterceptor implements Interceptor {

    private Context mContext;

    public InternetCacheInterceptor(Context mContext) {
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();
        if (isNetWork(mContext)) {
            // 只读缓存
            request = request.newBuilder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        return chain.proceed(request);
    }

    private boolean isNetWork(Context mContext) {
        //自己去完善
        return false;
    }
}
