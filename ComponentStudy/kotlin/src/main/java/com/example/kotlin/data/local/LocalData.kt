package com.example.kotlin.data.local

import android.content.Context
import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.login.LoginRequest
import com.example.kotlin.data.dto.login.LoginResponse
import com.example.kotlin.data.error.PASS_WORD_ERROR
import javax.inject.Inject

class LocalData @Inject constructor(val context:Context) {
    fun doLogin(loginRequest: LoginRequest):Resource<LoginResponse>{
        if(loginRequest == LoginRequest("coder@coder","coder")){
            return Resource.Success(LoginResponse("123","coder",21))
        }
        return Resource.DataError(PASS_WORD_ERROR)
    }
}