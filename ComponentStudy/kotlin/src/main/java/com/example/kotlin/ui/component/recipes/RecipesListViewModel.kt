package com.example.kotlin.ui.component.recipes

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlin.data.DataRepositorySource
import com.example.kotlin.data.Resource
import com.example.kotlin.data.dto.recipes.Recipes
import com.example.kotlin.data.dto.recipes.RecipesItem
import com.example.kotlin.ui.base.BaseViewModel
import com.example.kotlin.utils.SingleEvent
import com.example.kotlin.utils.wrapEspressoIdlingResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RecipesListViewModel @Inject
constructor(private val dataRepositoryRepository: DataRepositorySource) : BaseViewModel() {

    /**
     * Data --> LiveData, Exposed as LiveData,locally in viewModel as MutableLiveData
     */
    private val recipesLiveDataPrivate = MutableLiveData<Resource<Recipes>>()
    val recipesLiveData:LiveData<Resource<Recipes>> get() = recipesLiveDataPrivate

    private val recipesSearchFoundPrivate:MutableLiveData<RecipesItem> = MutableLiveData()
    val recipesSearchFound:LiveData<RecipesItem> get() = recipesSearchFoundPrivate

    private val noSearchFoundPrivate:MutableLiveData<Unit> = MutableLiveData()
    val noSearchFound:LiveData<Unit> get() = noSearchFoundPrivate

    /**
     * UI action as event, user action is single one time event,Shouldn't bt multiple time consumption
     */
    private val openRecipesDetailsPrivate = MutableLiveData<SingleEvent<RecipesItem>>()
    val openRecipesDetails:LiveData<SingleEvent<RecipesItem>> get() = openRecipesDetailsPrivate

    /**
     * Error handling as UI
     */

    private val showSnackBarPrivate = MutableLiveData<SingleEvent<Any>>()
    val showSnackBar:LiveData<SingleEvent<Any>> get() = showSnackBarPrivate

    private val showToastPrivate = MutableLiveData<SingleEvent<Any>>()
    val showToast:LiveData<SingleEvent<Any>> get() = showToastPrivate

    fun getRecipes(){
        viewModelScope.launch {
            recipesLiveDataPrivate.value = Resource.Loading()
            wrapEspressoIdlingResource {
                dataRepositoryRepository.requestRecipes().collect {
                    recipesLiveDataPrivate.value = it
                }
            }
        }
    }

    fun openRecipesDetails(recipes:RecipesItem){
        openRecipesDetailsPrivate.value = SingleEvent(recipes)
    }

    fun showToastMessage(errorCode:Int){
        val error = errorManager.getError(errorCode)
        showToastPrivate.value = SingleEvent(error.description)
    }

    fun onSearchClick(recipeName:String){
        recipesLiveDataPrivate.value?.data?.recipesList?.let {
            if (it.isNotEmpty()){
                for (recipe in it) {
                    if (recipe.name.toLowerCase(Locale.ROOT).contains(recipeName.toLowerCase(Locale.ROOT))){
                        recipesSearchFoundPrivate.value = recipe
                        return
                    }
                }
            }
        }
        return noSearchFoundPrivate.postValue(Unit)
    }
}
