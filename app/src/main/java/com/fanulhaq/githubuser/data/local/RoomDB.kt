/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fanulhaq.githubuser.data.local.detail.DetailDao
import com.fanulhaq.githubuser.data.local.detail.DetailModel
import com.fanulhaq.githubuser.data.local.pagekey.PageKey
import com.fanulhaq.githubuser.data.local.pagekey.PageKeyDao
import com.fanulhaq.githubuser.data.local.repos.ReposDao
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.data.local.search.SearchDao
import com.fanulhaq.githubuser.data.local.search.SearchModel

@Database(
    entities = [SearchModel::class, DetailModel::class, ReposModel::class, PageKey::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB: RoomDatabase() {
    abstract fun searchDao(): SearchDao
    abstract fun detailDao(): DetailDao
    abstract fun reposDao(): ReposDao
    abstract fun pageKeyDao(): PageKeyDao
}