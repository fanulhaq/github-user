/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser

import android.app.Application
import com.bumptech.glide.Glide
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    override fun onLowMemory() {
        super.onLowMemory()
        Glide.get(this).clearMemory()
        Glide.get(this).clearDiskCache()
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        Glide.get(this).trimMemory(level)
    }
}