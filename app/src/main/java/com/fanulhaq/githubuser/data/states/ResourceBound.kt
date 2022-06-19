/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.states

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import retrofit2.Response


/**
 * A repository which provides resource from local database as well as remote end point.
 *
 * [RESULT] represents the type for database.
 * [REQUEST] represents the type for network.
 */
@ExperimentalCoroutinesApi
abstract class ResourceBound<RESULT, REQUEST> {

    fun asFlow() = flow {

        emit(Resource.loading())
        emit(
            Resource.success(
                fetchFromLocal().first(),
                false)
        )

        val apiResponse = fetchFromRemote()
        val remoteBody = apiResponse.body()

        if(apiResponse.isSuccessful && remoteBody != null) {
            deleteData()
            saveRemoteData(remoteBody)
        } else {
            emit(Resource.error(apiResponse.message(), apiResponse.code()))
        }

        emitAll(
            fetchFromLocal().map {
                Resource.success(it, true)
            }
        )
    }.catch { e ->
        emit(Resource.error(e.message ?: "Unknown Error", -1))
        e.printStackTrace()
    }


    @WorkerThread
    protected abstract suspend fun deleteData()

    @WorkerThread
    protected abstract suspend fun saveRemoteData(response: REQUEST): Unit?

    @MainThread
    protected abstract fun fetchFromLocal(): Flow<RESULT>

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<REQUEST>
}
