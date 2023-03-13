package com.example.kotlin.data

import com.example.kotlin.data.dto.login.LoginRequest
import com.example.kotlin.data.dto.login.LoginResponse
import kotlinx.coroutines.flow.Flow

interface DataRepositorySource {
    // suspend fun requestRecipes():Flow<Resource<Recipies>>
    suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>>
    suspend fun addToFavourite(id:String):Flow<Resource<Boolean>>
    suspend fun isFavorite(id:String):Flow<Resource<Boolean>>
}