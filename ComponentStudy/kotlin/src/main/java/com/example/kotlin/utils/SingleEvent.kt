package com.example.kotlin.utils

open class SingleEvent<out T>(private val content:T) {

    var hasBeenHandled = false
        private set

    /**
     * Returns the content and prevents its use again
     */

    fun getContentIfNotHandled():T?{
        return if(hasBeenHandled){
            null
        }else{
            hasBeenHandled = true
            content
        }
    }
}