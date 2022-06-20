/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:SuppressLint("RestrictedApi")

package com.fanulhaq.githubuser.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.fanulhaq.githubuser.R
import com.fanulhaq.githubuser.ext.isLoadImageReady


@BindingAdapter("imageCircleUrl")
fun imageCircleUrl(view: ImageView, url: String?) {
    if(view.context.isLoadImageReady() &&!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.ic_circle_placeholder)
            .error(R.drawable.ic_circle_error)
            .override(320, 320)
            .circleCrop()
            .into(view)
    }
}