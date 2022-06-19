/*
 * Copyright (c) 2022 - Muchi (Irfanul Haq).
 */

package com.fanulhaq.githubuser.data.local.pagekey

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PageKeyDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(data: List<PageKey>)

    @Query("SELECT * FROM page_key WHERE id=:id AND username=:username")
    fun getRemoteKeyWithUsername(id: Int, username: String): PageKey?

    @Query("SELECT * FROM page_key WHERE username=:username ORDER BY id DESC LIMIT 1")
    fun getLastRemoteKeyWithUsername(username: String): PageKey?

    @Query("DELETE FROM page_key WHERE username=:username")
    suspend fun deleteWithUsername(username: String)
}