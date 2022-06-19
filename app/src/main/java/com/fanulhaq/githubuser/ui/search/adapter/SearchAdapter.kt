/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("DEPRECATION")

package com.fanulhaq.githubuser.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.databinding.ItemSearchBinding
import com.fanulhaq.githubuser.ui.base.BaseRecyclerViewAdapter
import com.fanulhaq.githubuser.ui.base.BaseRecyclerViewHolder
import javax.inject.Inject

class SearchAdapter @Inject constructor() : BaseRecyclerViewAdapter<SearchModel, SearchAdapter.ViewHolder>() {

    var listener: OnSearchListener? = null

    override fun createItemViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun bindItemViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(
        private val binding: ItemSearchBinding
    ) : BaseRecyclerViewHolder<SearchModel>(binding.root)  {

        override fun bind(item: SearchModel) {
            with(binding) {
                data = item
                layoutContent.setOnClickListener {
                    listener?.onSearchListener(binding, adapterPosition, item)
                }
            }
        }
    }

    interface OnSearchListener {
        fun onSearchListener(binding: ItemSearchBinding, position: Int, data: SearchModel)
    }
}