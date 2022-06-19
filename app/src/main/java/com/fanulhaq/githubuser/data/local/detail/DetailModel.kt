/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.detail

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "detail")
data class DetailModel(
    @PrimaryKey val id: Int,
    val name: String?,
    @SerializedName("login") val username: String?,
    @SerializedName("avatar_url") val avatar: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    var bio: String?,
    var followers: String?,
    var following: String?,
)