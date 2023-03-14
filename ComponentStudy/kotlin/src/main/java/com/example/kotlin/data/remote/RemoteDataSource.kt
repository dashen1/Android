package com.example.kotlin.data.remote

import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.recipes.Recipes

internal interface RemoteDataSource {
    suspend fun requestRecipes(): Resource<Recipes>
}