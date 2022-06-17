/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.fanulhaq.githubuser.utils

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class NetworkConnectionInterceptor(
    context: Context,
    isAuth: Boolean = false
) : Interceptor {

    private val mContext: Context = context
    private val mIsAuth: Boolean = isAuth

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected)
            throw NoConnectionException()

        val original = chain.request()
        val builder =  original.newBuilder()
        if(mIsAuth) {
            builder.apply {
                header("Content-Type", "application/json")
                header("Authorization", Credentials.basic("", ""))
                method(original.method, original.body)
            }
        }
        return chain.proceed(builder.build())
    }

    private val isConnected: Boolean
        get() {
            val connectivityManager = mContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnected
        }

    inner class NoConnectionException : IOException() {
        override val message: String
            get() = "No Internet Connection"
    }
}