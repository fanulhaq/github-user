/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.search

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<SearchModel>)

    @Query("SELECT * FROM search WHERE username LIKE '%' || :key || '%'")
    fun pagingSource(key: String): PagingSource<Int, SearchModel>

    @Query("DELETE FROM search")
    suspend fun deleteAll()

    @Query("SELECT * FROM search WHERE username LIKE '%' || :key || '%'")
    fun getDataWithKey(key: String): Flow<List<SearchModel>>

    @Query("DELETE FROM search WHERE username LIKE '%' || :key || '%'")
    suspend fun deleteWithKey(key: String)
}