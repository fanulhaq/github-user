/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.repository.search

import com.fanulhaq.githubuser.data.local.search.SearchDao
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.response.SearchResponse
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.data.states.ResourceBound
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class SearchRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val searchDao: SearchDao
) : SearchRepo {
    override suspend fun search(q: String): Flow<Resource<List<SearchModel>>> {
        return object : ResourceBound<List<SearchModel>, SearchResponse<SearchModel>>() {
            override suspend fun saveRemoteData(response: SearchResponse<SearchModel>) = response.items?.let { searchDao.insertAll(it) }
            override suspend fun fetchFromRemote(): Response<SearchResponse<SearchModel>> = githubApi.search(q = q, per_page = 20)
            override suspend fun deleteData() = searchDao.deleteWithKey(q)
            override fun fetchFromLocal(): Flow<List<SearchModel>> = searchDao.getDataWithKey(q)
        }.asFlow()
    }
}