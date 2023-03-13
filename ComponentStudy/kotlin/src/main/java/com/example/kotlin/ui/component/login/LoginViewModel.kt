package com.example.kotlin.ui.component.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.DataRepository
import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.login.LoginRequest
import com.example.kotlin.data.dto.login.LoginResponse
import com.example.kotlin.data.error.CHECK_YOUR_FIELDS
import com.example.kotlin.data.error.PASS_WORD_ERROR
import com.example.kotlin.data.error.USER_NAME_ERROR
import com.example.kotlin.ui.base.BaseViewModel
import com.example.kotlin.utils.RegexUtils.isValidEmail
import com.example.kotlin.utils.SingleEvent
import com.example.kotlin.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository:DataRepository): BaseViewModel(){

    private val loginLiveDataPrivate = MutableLiveData<Resource<LoginResponse>>()
    val loginLiveData: LiveData<Resource<LoginResponse>> get() = loginLiveDataPrivate

    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast:LiveData<SingleEvent<Any>> get() = showToastPrivate

    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar:LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    /** Error handling as UI **/

    fun doLogin(userName:String,passWord:String){
        val isUsernameValid = isValidEmail(userName)
        val isPassWordValid = passWord.trim().length>4
        if (isUsernameValid&&!isPassWordValid){
            loginLiveDataPrivate.value = Resource.DataError(PASS_WORD_ERROR)
        }else if (!isUsernameValid&&isPassWordValid){
            loginLiveDataPrivate.value = Resource.DataError(USER_NAME_ERROR)
        }else if (!isUsernameValid&&!isPassWordValid){
            loginLiveDataPrivate.value = Resource.DataError(CHECK_YOUR_FIELDS)
        }else{
            viewModelScope.launch {
                loginLiveDataPrivate.value = Resource.Loading()
                wrapEspressoIdlingResource {
                    dataRepository.doLogin(loginRequest = LoginRequest(userName,passWord)).collect {
                        loginLiveDataPrivate.value = it
                        Log.d("LoginViewModel","return times")
                    }
                }
            }
        }
    }

    fun showToastMessage(errorCode:Int){
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }
}