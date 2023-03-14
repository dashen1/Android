package com.example.kotlin.ui.component.recipes.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.R
import com.example.kotlin.data.dto.recipes.RecipesItem
import com.example.kotlin.databinding.RecipeItemBinding
import com.example.kotlin.ui.base.listener.RecyclerItemListener
import com.squareup.picasso.Picasso

class RecipeViewHolder(private val itemBinding: RecipeItemBinding) :
    RecyclerView.ViewHolder(itemBinding.root) {

    fun bind(recipesItem: RecipesItem, recyclerItemListener: RecyclerItemListener) {
        itemBinding.tvCaption.text = recipesItem.description
        itemBinding.tvName.text = recipesItem.name
        Picasso.get().load(recipesItem.thumb).placeholder(R.drawable.ic_healthy_food)
            .error(R.drawable.ic_healthy_food).into(itemBinding.ivRecipeItemImage)
        itemBinding.rlRecipesItem.setOnClickListener {
            recyclerItemListener.onItemSelected(
                recipesItem
            )
        }
    }
}