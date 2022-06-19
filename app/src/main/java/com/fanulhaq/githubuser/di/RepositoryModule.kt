/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.di

import androidx.paging.ExperimentalPagingApi
import com.fanulhaq.githubuser.data.repository.detail.DetailRepo
import com.fanulhaq.githubuser.data.repository.detail.DetailRepoImpl
import com.fanulhaq.githubuser.data.repository.search.SearchRepo
import com.fanulhaq.githubuser.data.repository.search.SearchRepoImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindSearchRepository(searchRepoImpl: SearchRepoImpl): SearchRepo

    @Binds
    @ExperimentalPagingApi
    abstract fun bindDetailRepository(detailRepoImpl: DetailRepoImpl): DetailRepo
}