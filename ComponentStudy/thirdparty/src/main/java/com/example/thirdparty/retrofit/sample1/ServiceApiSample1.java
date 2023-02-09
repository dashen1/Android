package com.example.thirdparty.retrofit.sample1;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ServiceApiSample1 {

    @POST("register")
    Call<UserLoginResult> userRegister(
            @Query("username") String userName,
            @Query("password") String userPwd,
            @Query("repassword") String userRePwd
    );

    @POST("login")
    Call<UserLoginResult> userLogin(
            @Query("username") String userName,
            @Query("password") String userPwd
    );

    @POST("login")
    Observable<UserLoginResult> userLoginWithRxjava(
            @Query("username") String userName,
            @Query("password") String userPwd
    );
}
