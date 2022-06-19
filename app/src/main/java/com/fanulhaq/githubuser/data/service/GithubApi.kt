/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.service

import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.response.DetailResponse
import com.fanulhaq.githubuser.data.response.SearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {
    @GET("search/users")
    suspend fun search(
        @Query("q") q: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Response<SearchResponse<SearchModel>>

    @GET("user/{username}")
    suspend fun detail(
        @Path(value = "username", encoded = true) username: String,
    ): Response<DetailResponse>

    @GET("{username}/repos")
    suspend fun repo(
        @Path(value = "username", encoded = true) username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = 30
    ): Response<DetailResponse>
}