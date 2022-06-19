/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.repos

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "repos")
data class ReposModel(
    @PrimaryKey val id: Int,
    val name: String?,
    var username: String?,
    var updated_at: String?,
    @SerializedName("stargazers_count") var star: String?,
    val description: String?
)