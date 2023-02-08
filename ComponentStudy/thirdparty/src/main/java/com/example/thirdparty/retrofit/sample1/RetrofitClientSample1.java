package com.example.thirdparty.retrofit.sample1;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientSample1 {
    private static final ServiceApiSample1 mServiceApi;

    static {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.wanandroid.com/user/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        mServiceApi = retrofit.create(ServiceApiSample1.class);
    }

    public static ServiceApiSample1 getServiceApi(){
        return mServiceApi;
    }
}
