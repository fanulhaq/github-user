/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.fanulhaq.githubuser.utils

import android.content.Context
import android.net.ConnectivityManager
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException


class NetworkConnectionInterceptor(
    context: Context
) : Interceptor {

    private val mContext: Context = context

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected)
            throw NoConnectionException()

        val original = chain.request()
        val builder =  original.newBuilder()
        builder.apply {
            header("Accept", "application/vnd.github.v3+json")
            method(original.method, original.body)
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