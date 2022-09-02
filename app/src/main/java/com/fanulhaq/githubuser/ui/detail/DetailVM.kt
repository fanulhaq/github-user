/*
 * Copyright (c) 2022 - Muchi (Irfanul Haq).
 */

package com.fanulhaq.githubuser.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.repository.detail.DetailRepoImpl
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class DetailVM @Inject constructor(
    private val repository: DetailRepoImpl
): BaseViewModel() {

    private var _detail = MutableLiveData<Resource<DetailModel>>()
    val detail: LiveData<Resource<DetailModel>>
        get() = _detail

    fun detail(username: String) {
        viewModelScope.launch {
            repository.detail(username).collect {
                _detail.postValue(it)
            }
        }
    }

    private var _dataDetail = MutableLiveData<DetailModel>()
    val dataDetail: LiveData<DetailModel>
        get() = _dataDetail

    fun dataDetail(value: DetailModel) {
        _dataDetail.value = value
    }

    private var _repos: Flow<PagingData<ReposModel>> = flow { PagingData.empty<ReposModel>() }
    val repos: Flow<PagingData<ReposModel>>
        get() = _repos

    fun repos(username: String) = launchPagingAsync({
        repository.repos(username).cachedIn(viewModelScope)
    }, {
        _repos = it
    })
}