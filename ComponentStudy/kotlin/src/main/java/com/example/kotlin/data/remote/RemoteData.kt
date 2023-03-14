package com.example.kotlin.data.remote

import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.recipes.Recipes
import com.example.kotlin.data.dto.recipes.RecipesItem
import com.example.kotlin.data.error.NETWORK_ERROR
import com.example.kotlin.data.error.NO_INTERNET_CONNECTION
import com.example.kotlin.data.remote.service.RecipesService
import com.example.kotlin.utils.NetWorkConnectivity
import retrofit2.Response
import java.io.IOError
import java.io.IOException
import javax.inject.Inject

class RemoteData @Inject
constructor(
    private val serviceGenerator: ServiceGenerator,
    private val netWorkConnectivity: NetWorkConnectivity
) : RemoteDataSource {
    override suspend fun requestRecipes(): Resource<Recipes> {
        val recipesService = serviceGenerator.createService(RecipesService::class.java)
        return when (val response = processCall(recipesService::fetchRecipes)) {
            is List<*> -> {
                Resource.Success(data = Recipes(response as ArrayList<RecipesItem> /* = java.util.ArrayList<com.example.kotlin.data.dto.recipes.RecipesItem> */))
            }
            else -> {
                Resource.DataError(errorCode = response as Int)
            }
        }
    }

    private suspend fun processCall(responseCall: suspend () -> Response<*>): Any? {
        if (!netWorkConnectivity.isConnected()) {
            return NO_INTERNET_CONNECTION
        }
        return try {
            val response = responseCall.invoke()
            val responseCode = response.code()
            if (response.isSuccessful) {
                response.body()
            } else {
                responseCode
            }
        } catch (e: IOException) {
            NETWORK_ERROR
        }
    }

}