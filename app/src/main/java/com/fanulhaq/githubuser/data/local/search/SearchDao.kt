/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.search

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SearchDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<SearchModel>)

    @Query("SELECT * FROM search WHERE username LIKE '%' || :key || '%' LIMIT 30")
    fun getDataWithKey(key: String): Flow<List<SearchModel>>

    @Query("DELETE FROM search WHERE username LIKE '%' || :key || '%'")
    suspend fun deleteWithKey(key: String)
}