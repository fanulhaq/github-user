package com.fanulhaq.githubuser.db

import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.local.search.SearchModel

class AppDatabaseFake {
    val search = mutableListOf<SearchModel>()
    val repos = mutableListOf<ReposModel>()
    val detail = mutableListOf<DetailModel>()
}