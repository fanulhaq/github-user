/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.fanulhaq.githubuser.ui.detail.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fanulhaq.githubuser.data.local.repos.ReposModel
import com.fanulhaq.githubuser.databinding.ItemUserReposBinding
import com.fanulhaq.githubuser.ui.base.BaseRecyclerViewHolder
import javax.inject.Inject


class ReposAdapter @Inject constructor() : PagingDataAdapter<ReposModel, ReposAdapter.ViewHolder>(ReposComparator) {

    var avatar: String? = null

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemUserReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    inner class ViewHolder(
        private val binding: ItemUserReposBinding
    ) : BaseRecyclerViewHolder<ReposModel>(binding.root)  {
        override fun bind(item: ReposModel) {
            with(binding) {
                data = item
                urlAvatar = avatar
            }
        }
    }

    object ReposComparator : DiffUtil.ItemCallback<ReposModel>() {
        override fun areItemsTheSame(oldItem: ReposModel, newItem: ReposModel) = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: ReposModel, newItem: ReposModel) = oldItem == newItem
    }
}