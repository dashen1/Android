package com.example.kotlin.usercase.errors

import com.example.kotlin.data.error.Error


interface ErrorUseCase {
    fun getError(errorCode: Int):Error
}