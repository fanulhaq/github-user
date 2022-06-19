/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.pagekey

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "page_key")
data class PageKey(
    @PrimaryKey
    val id: Int,
    val username: String,
    val prev: Int?,
    val next: Int?
)
