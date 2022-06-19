/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.ui.detail

import android.util.Log
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import com.fanulhaq.githubuser.R
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.databinding.FragmentDetailBinding
import com.fanulhaq.githubuser.ext.toast
import com.fanulhaq.githubuser.ui.base.BaseFragment
import com.fanulhaq.githubuser.ui.detail.adapter.ReposAdapter
import com.fanulhaq.githubuser.utils.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {

    private val viewModel: DetailVM by viewModels()
    private val args: DetailFragmentArgs by navArgs()
    @Inject lateinit var reposAdapter: ReposAdapter

    override fun initViews() {
        super.initViews()
        with(binding) {
            lifecycleOwner = this@DetailFragment
            viewmodel = viewModel

            recyclerView.adapter = reposAdapter.withLoadStateFooter(PagingLoadStateAdapter(reposAdapter))
            reposAdapter.avatar = args.data.avatar
            swipeRefresh.setOnRefreshListener {
                viewModel.detail(args.data.username)
                reposAdapter.refresh()
            }
            imageView.setOnClickListener {
                Log.d("ReposPagingLog", "${args.data.username} == ${reposAdapter.itemCount}")
            }
        }
        subscribeToObservables()
    }

    private fun subscribeToObservables() {
        with(binding) {
            viewModel.detail.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is Resource.Loading -> {
                        swipeRefresh.isRefreshing = true
                    }
                    is Resource.Success -> {
                        if(state.finishLoading) {
                            swipeRefresh.isRefreshing = false
                        }
                        try {
                            viewModel.dataDetail(state.data)
                            visibleView()
                        } catch (e: NullPointerException) {
                            e.printStackTrace()
                        }
                    }
                    is Resource.Error -> {
                        swipeRefresh.isRefreshing = false
                        context.toast(state.message.ifEmpty { "Error ${state.code}" })
                    }
                }
            }
            viewModel.detail(args.data.username)

            launchOnLifecycleScope {
                viewModel.repos.collectLatest {
                    reposAdapter.submitData(it)
                }
            }
            launchOnLifecycleScope {
                reposAdapter.loadStateFlow.collectLatest {
                    swipeRefresh.isRefreshing = it.refresh is LoadState.Loading
                    if(it.refresh is LoadState.Error) {
                        context.toast("${(it.refresh as? LoadState.Error)?.error?.message}")
                    }
                }
            }
            launchOnLifecycleScope {
                reposAdapter.loadStateFlow.distinctUntilChangedBy { it.refresh }
                    .filter { it.refresh is LoadState.NotLoading }
                    .collect { recyclerView.scrollToPosition(0) }
            }
//            viewModel.repos(args.data.username)
        }
    }

    private fun visibleView() = with(binding) {
        viewLine.isVisible = true
        layoutEmail.isVisible = true
        layoutLocation.isVisible = true
        layoutFollow.isVisible = true
        tvBio.isVisible = true
        layoutHeader.isVisible = true
    }
}