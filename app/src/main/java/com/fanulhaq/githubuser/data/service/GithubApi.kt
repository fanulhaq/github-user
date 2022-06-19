/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.service

import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.paging.NETWORK_PAGE_SIZE
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

    @GET("users/{username}")
    suspend fun detail(
        @Path(value = "username", encoded = true) username: String,
    ): Response<DetailModel>

    @GET("users/{username}/repos")
    suspend fun repos(
        @Path(value = "username", encoded = true) username: String,
        @Query("page") page: Int = 1,
        @Query("per_page") per_page: Int = NETWORK_PAGE_SIZE
    ): Response<List<ReposModel>>
}