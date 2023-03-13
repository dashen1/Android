package com.example.kotlin.di

import com.example.kotlin.data.error.mapper.ErrorMapper
import com.example.kotlin.data.error.mapper.ErrorMapperSource
import com.example.kotlin.usercase.errors.ErrorManager
import com.example.kotlin.usercase.errors.ErrorUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorModule {
    @Binds
    @Singleton
    abstract fun provideErrorFactoryImpl(errorManager: ErrorManager): ErrorUseCase

    @Binds
    @Singleton
    abstract fun provideErrorMapper(errorMapper: ErrorMapper): ErrorMapperSource
}