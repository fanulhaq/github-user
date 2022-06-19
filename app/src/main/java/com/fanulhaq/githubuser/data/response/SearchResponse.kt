/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.data.response

data class SearchResponse<T>(
    val message: String?,
    val items: List<T>?
)