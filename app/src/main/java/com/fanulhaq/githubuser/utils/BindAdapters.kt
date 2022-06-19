/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.utils

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.fanulhaq.githubuser.R
import com.fanulhaq.githubuser.ext.isLoadImageReady
import com.fanulhaq.githubuser.ext.numberShortFormatter


@BindingAdapter("imageCircleUrl")
fun imageCircleUrl(view: ImageView, url: String?) {
    if(view.context.isLoadImageReady() &&!url.isNullOrEmpty()) {
        Glide.with(view.context)
            .load(url)
            .placeholder(R.drawable.ic_circle)
            .error(R.drawable.ic_circle)
            .transition(withCrossFade())
            .override(320, 320)
            .circleCrop()
            .into(view)
    }
}

@BindingAdapter("numberShortFormatter")
fun numberShortFormatter(view: TextView, value: Int) {
    view.text = value.numberShortFormatter()
}