/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.di

import android.content.Context
import com.fanulhaq.githubuser.BuildConfig.DEBUG
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.utils.NetworkConnectionInterceptor
import com.google.gson.FieldNamingPolicy.IDENTITY
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit.SECONDS
import javax.inject.Named
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
        if(DEBUG) {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder()
            .setFieldNamingPolicy(IDENTITY)
            .serializeNulls()
            .setLenient()
            .create()

    @Provides
    @Singleton
    @Named("OkHttpClient")
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder().apply {
            interceptors().add(httpLoggingInterceptor)
            interceptors().add(NetworkConnectionInterceptor(context))
            connectTimeout(30, SECONDS)
            readTimeout(30, SECONDS)
            writeTimeout(30, SECONDS)
        }.build()

    @Provides
    @Singleton
    @Named("RetrofitGithub")
    fun provideRetrofitGithub(
        gson: Gson,
        @Named("OkHttpClient") okHttpClient: OkHttpClient
    ): Retrofit = Retrofit.Builder().apply {
            baseUrl("https://api.github.com/")
            addConverterFactory(GsonConverterFactory.create(gson))
            client(okHttpClient)
        }.build()

    @Provides
    @Singleton
    fun provideGithubApi(@Named("RetrofitGithub") retrofit: Retrofit):
            GithubApi = retrofit.create(GithubApi::class.java)
}