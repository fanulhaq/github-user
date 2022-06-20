/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.interactors

import com.fanulhaq.githubuser.data.local.detail.DetailDao
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.db.AppDatabaseFake
import com.fanulhaq.githubuser.db.DetailDaoFake
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@ExperimentalCoroutinesApi
class DetailTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer

    private lateinit var service: GithubApi
    private lateinit var dao: DetailDao

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GithubApi::class.java)

        dao = DetailDaoFake(appDatabase)
    }

    @Test
    fun success_data() {
    }
}