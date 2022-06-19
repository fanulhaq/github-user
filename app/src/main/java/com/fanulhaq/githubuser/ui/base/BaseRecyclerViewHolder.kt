/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.fanulhaq.githubuser.ui.base

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewHolder<T>(
    containerView: View
) : RecyclerView.ViewHolder(containerView) {
    abstract fun bind(item: T)
}
