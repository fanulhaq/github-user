/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.repos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ReposDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<ReposModel>)

    @Query("SELECT * FROM repos WHERE username=:username")
    fun pagingSourceWithUsername(username: String): PagingSource<Int, ReposModel>

    @Query("DELETE FROM repos WHERE username=:username")
    suspend fun deleteWithUsername(username: String)
}