/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.repository.detail

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.fanulhaq.githubuser.data.local.RoomDB
import com.fanulhaq.githubuser.data.local.detail.DetailDao
import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.repos.ReposDao
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.paging.NETWORK_PAGE_SIZE
import com.fanulhaq.githubuser.data.paging.ReposMediator
import com.fanulhaq.githubuser.data.service.GithubApi
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.data.states.ResourceBound
import com.fanulhaq.githubuser.ext.numberShortFormatter
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
class DetailRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
    private val detailDao: DetailDao,
    private val reposDao: ReposDao,
    private var db: RoomDB
) : DetailRepo {

    override suspend fun detail(username: String): Flow<Resource<DetailModel>> {
        return object : ResourceBound<DetailModel, DetailModel>() {
            override suspend fun saveRemoteData(response: DetailModel) {
                response.apply {
                    if(!company.isNullOrEmpty()) bio = "$company ${
                        if(!bio.isNullOrEmpty()) ", $bio" else ""
                    }"
                    followers = followers?.toLong()?.numberShortFormatter()
                    following = following?.toLong()?.numberShortFormatter()
                }
                detailDao.insert(response)
            }
            override suspend fun fetchFromRemote(): Response<DetailModel> = githubApi.detail(username)
            override suspend fun deleteData() = detailDao.deleteWithUsername(username)
            override fun fetchFromLocal(): Flow<DetailModel> = detailDao.getDataWithUsername(username)
        }.asFlow()
    }

    override suspend fun repos(username: String): Flow<PagingData<ReposModel>> = Pager(
        config = PagingConfig(pageSize = NETWORK_PAGE_SIZE, prefetchDistance = 2*NETWORK_PAGE_SIZE),
        remoteMediator = ReposMediator(githubApi, db, username)
    ) {
        reposDao.pagingSourceWithUsername(username)
    }.flow
}