package com.example.kotlin.data

import com.example.kotlin.data.remote.RemoteData
import javax.inject.Inject

class DataRepository @Inject constructor(private val remoteRepository:RemoteData){
}