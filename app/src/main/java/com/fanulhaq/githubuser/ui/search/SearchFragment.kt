/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.ui.search

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.fanulhaq.githubuser.R
import com.fanulhaq.githubuser.data.local.search.SearchModel
import com.fanulhaq.githubuser.data.states.Resource
import com.fanulhaq.githubuser.databinding.FragmentSearchBinding
import com.fanulhaq.githubuser.databinding.ItemSearchBinding
import com.fanulhaq.githubuser.ext.toast
import com.fanulhaq.githubuser.ui.base.BaseFragment
import com.fanulhaq.githubuser.ui.search.adapter.SearchAdapter
import com.fanulhaq.githubuser.ui.search.adapter.SearchAdapter.OnSearchListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search), OnSearchListener {

    private val viewModel: SearchVM by viewModels()
    @Inject lateinit var searchAdapter: SearchAdapter

    private var afterMoveToDetail = false

    override fun initViews() {
        super.initViews()
        binding.recyclerView.adapter = searchAdapter
        searchAdapter.listener = this
        initSearch()
        subscribeToObservables()
    }

    private fun subscribeToObservables() = with(binding) {
        viewModel.search.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    progressBar.isVisible = true
                    searchAdapter.clearAdapter()
                }
                is Resource.Success -> {
                    if(state.finishLoading) progressBar.isVisible = false
                    if(!state.data.isNullOrEmpty()) {
                        searchAdapter.addAll(state.data as ArrayList<SearchModel>, true)
                    }
                }
                is Resource.Error -> {
                    progressBar.isVisible = false
                    context.toast(state.message.ifEmpty { "Error ${state.code}" })
                }
            }
        }
    }

    private fun initSearch() = with(binding) {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                if(s.isEmpty()) {
                    searchAdapter.clearAdapter()
                } else {
                    if(!afterMoveToDetail) viewModel.search("$s")
                }
            }
        })

        etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == IME_ACTION_SEARCH) {
                if (!etSearch.text.isNullOrEmpty()) {
                    viewModel.search("${etSearch.text}")
                }
                return@OnEditorActionListener true
            }
            false
        })
    }

    override fun onResume() {
        super.onResume()
        afterMoveToDetail = false
    }

    override fun onSearchListener(binding: ItemSearchBinding, position: Int, data: SearchModel) {
        afterMoveToDetail = true
        findNavController().navigate(SearchFragmentDirections.actionSearchToDetail(data))
    }
}