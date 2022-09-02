/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.interactors

import com.fanulhaq.githubuser.data.local.search.SearchDao
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.db.AppDatabaseFake
import com.fanulhaq.githubuser.db.SearchDaoFake
import com.fanulhaq.githubuser.response.SearchResponse.successData
import com.fanulhaq.githubuser.response.SearchResponse.successDataKey
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

@ExperimentalCoroutinesApi
class SearchTest {

    private val appDatabase = AppDatabaseFake()
    private lateinit var mockWebServer: MockWebServer

    private lateinit var service: GithubApi
    private lateinit var dao: SearchDao

    @Before
    fun setup() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        service = Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(GithubApi::class.java)

        dao = SearchDaoFake(appDatabase)
    }

    @Test
    fun success_data() = runBlocking {
        mockWebServer.enqueue(
            MockResponse()
                .setResponseCode(HttpURLConnection.HTTP_OK)
                .setBody(successData)
        )

        var cachedLocal: List<SearchModel>? = null
        dao.getDataWithKey(successDataKey).collect { cachedLocal = it }
        assert(cachedLocal.isNullOrEmpty())

        val api = service.search(successDataKey)
        assert(!api.body()?.items.isNullOrEmpty())
        dao.deleteWithKey(successDataKey)
        api.body()?.items?.let { dao.insertAll(it) }

        var cachedLocalAfterRemote: List<SearchModel>? = null
        dao.getDataWithKey(successDataKey).collect { cachedLocalAfterRemote = it }
        assert(!cachedLocalAfterRemote.isNullOrEmpty())
    }
}