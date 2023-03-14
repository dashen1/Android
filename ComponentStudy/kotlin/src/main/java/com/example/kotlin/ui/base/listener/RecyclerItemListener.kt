package com.example.kotlin.ui.base.listener

import com.example.kotlin.data.dto.recipes.RecipesItem

interface RecyclerItemListener {
    fun onItemSelected(recipe: RecipesItem)
}