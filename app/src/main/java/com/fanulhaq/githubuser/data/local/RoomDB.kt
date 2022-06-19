/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.fanulhaq.githubuser.data.local.search.SearchDao
import com.fanulhaq.githubuser.data.local.search.SearchModel

@Database(
    entities = [SearchModel::class],
    version = 1,
    exportSchema = false
)
abstract class RoomDB: RoomDatabase() {
    abstract fun searchDao(): SearchDao
}