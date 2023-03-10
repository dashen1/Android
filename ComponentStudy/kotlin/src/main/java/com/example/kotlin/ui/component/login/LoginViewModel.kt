package com.example.kotlin.ui.component.login

import com.example.kotlin.data.DataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val dataRepository:DataRepository){
}