/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.ui.search

import com.fanulhaq.githubuser.R
import com.fanulhaq.githubuser.databinding.FragmentSearchBinding
import com.fanulhaq.githubuser.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    override fun initViews() {
        super.initViews()
        with(binding) {
            thisFragment = this@SearchFragment
        }
    }
}