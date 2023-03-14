package com.example.kotlin.data.remote.service

import com.example.kotlin.data.dto.recipes.RecipesItem
import retrofit2.Response
import retrofit2.http.GET

interface RecipesService {
    @GET("recipes.json")
    suspend fun fetchRecipes(): Response<List<RecipesItem>>
}