/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.detail

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DetailDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: DetailModel)

    @Query("DELETE FROM detail WHERE username=:username")
    suspend fun deleteWithUsername(username: String)

    @Query("SELECT * FROM detail WHERE username=:username")
    fun getDataWithUsername(username: String): Flow<DetailModel>
}