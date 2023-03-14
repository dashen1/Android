package com.example.kotlin.ui.component.details

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.DataRepositorySource
import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.recipes.RecipesItem
import com.example.kotlin.ui.base.BaseViewModel
import com.example.kotlin.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class DetailsViewModel @Inject constructor(private val dataRepository: DataRepositorySource) :
    BaseViewModel() {

    private val recipePrivate = MutableLiveData<RecipesItem>()
    val recipeData: LiveData<RecipesItem> get() = recipePrivate

    private val isFavouritePrivate = MutableLiveData<Resource<Boolean>>()
    val isFavourite: LiveData<Resource<Boolean>> get() = isFavouritePrivate

    fun initIntentData(recipe: RecipesItem) {
        recipePrivate.value = recipe
    }

    open fun addToFavourite() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.addToFavourite(it).collect { isAdded ->
                        isFavouritePrivate.value = isAdded
                    }
                }
            }
        }
    }

    fun removeFromFavourite() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.removeFromFavourite(it).collect { isRemoved ->
                        when (isRemoved) {
                            is Resource.Success -> {
                                isRemoved.data?.let {
                                    isFavouritePrivate.value = Resource.Success(!isRemoved.data)
                                }
                            }
                            is Resource.DataError -> {
                                isFavouritePrivate.value = isRemoved
                            }
                            is Resource.Loading -> {
                                isFavouritePrivate.value = isRemoved
                            }
                        }
                    }
                }
            }
        }
    }

    fun isFavourite() {
        viewModelScope.launch {
            isFavouritePrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                recipePrivate.value?.id?.let {
                    dataRepository.isFavorite(it).collect { isFavourite ->
                        isFavouritePrivate.value = isFavourite
                    }
                }
            }
        }
    }
}