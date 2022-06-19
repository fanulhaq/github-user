/*
 * Copyright (c) 2022 - Muchi (Irfanul Haq).
 */

package com.fanulhaq.githubuser.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.repository.search.SearchRepoImpl
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.ui.base.BaseViewModel
import com.fanulhaq.githubuser.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
@ExperimentalCoroutinesApi
class SearchVM @Inject constructor(
    private val repository: SearchRepoImpl
): BaseViewModel() {

    private var _search = SingleLiveEvent<Resource<List<SearchModel>>>()
    val search: LiveData<Resource<List<SearchModel>>>
        get() = _search

    fun search(q: String) {
        viewModelScope.launch {
            repository.search(q).collect {
                _search.postValue(it)
            }
        }
    }
}