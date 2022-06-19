/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.states

sealed class Resource<T> {

    class Loading<T> : Resource<T>()
    data class Success<T>(val data: T, val finishLoading: Boolean) : Resource<T>()
    data class Error<T>(val message: String, val code: Int) : Resource<T>()

    companion object {
        fun <T> loading() = Loading<T>()
        fun <T> success(data: T, finishLoading: Boolean) = Success(data, finishLoading)
        fun <T> error(message: String, code: Int) = Error<T>(message, code)
    }
}