/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.repository.detail

import androidx.paging.PagingData
import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.states.Resource
import kotlinx.coroutines.flow.Flow

interface DetailRepo {
    suspend fun detail(username: String) : Flow<Resource<DetailModel>>
    suspend fun repos(username: String) : Flow<PagingData<ReposModel>>
}