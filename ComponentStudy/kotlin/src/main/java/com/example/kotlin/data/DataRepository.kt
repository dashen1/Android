package com.example.kotlin.data

import com.example.kotlin.data.dto.login.LoginRequest
import com.example.kotlin.data.dto.login.LoginResponse
import com.example.kotlin.data.local.LocalData
import com.example.kotlin.data.remote.RemoteData
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.log

class DataRepository @Inject constructor(
    private val remoteRepository: RemoteData,
    private val localRepository: LocalData,
    private val ioDispatcher: CoroutineContext
) : DataRepositorySource {

    override suspend fun doLogin(loginRequest: LoginRequest): Flow<Resource<LoginResponse>> {
        return flow {
            emit(localRepository.doLogin(loginRequest))
        }.flowOn(ioDispatcher)
    }

    override suspend fun addToFavourite(id: String): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }

    override suspend fun isFavorite(id: String): Flow<Resource<Boolean>> {
        TODO("Not yet implemented")
    }
}