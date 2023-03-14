package com.example.kotlin.ui.component.recipes.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlin.data.dto.recipes.RecipesItem
import com.example.kotlin.databinding.RecipeItemBinding
import com.example.kotlin.ui.base.listener.RecyclerItemListener
import com.example.kotlin.ui.component.recipes.RecipesListViewModel

class RecipeAdapter(private val recipesListViewModel: RecipesListViewModel,private val recipes:List<RecipesItem>):RecyclerView.Adapter<RecipeViewHolder>(){

    private val onItemClickListener:RecyclerItemListener = object :RecyclerItemListener{

        override fun onItemSelected(recipe: RecipesItem) {
            recipesListViewModel.openRecipesDetails(recipe)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val itemBinding = RecipeItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecipeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(recipes[position],onItemClickListener)
    }

    override fun getItemCount(): Int {
        return recipes.size
    }

}