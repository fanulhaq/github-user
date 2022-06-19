/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.local.search

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "search")
data class SearchModel (
    @PrimaryKey val id: Int,
    @SerializedName("login") val username: String,
    @SerializedName("avatar_url") val avatar: String,
    @SerializedName("html_url") val url: String
) : Parcelable