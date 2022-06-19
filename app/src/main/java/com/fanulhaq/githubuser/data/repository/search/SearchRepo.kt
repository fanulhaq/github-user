/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.repository.search

import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.states.Resource
import kotlinx.coroutines.flow.Flow

interface SearchRepo {
    suspend fun search(q: String) : Flow<Resource<List<SearchModel>>>
}