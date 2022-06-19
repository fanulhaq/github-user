/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.ui.search

import android.text.Editable
import android.text.TextWatcher
import android.view.View.GONE
import android.view.View.VISIBLE
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

    override fun initViews() {
        super.initViews()
        with(binding) {
            thisFragment = this@SearchFragment
            recyclerView.adapter = searchAdapter
            searchAdapter.listener = this@SearchFragment

            etSearch.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
                override fun afterTextChanged(s: Editable) {
                    if(s.isEmpty()) {
                        searchAdapter.clearAdapter()
                    } else {
                        viewModel.search("$s")
                    }
                }
            })

//            etSearch.setOnEditorActionListener(OnEditorActionListener { _, actionId, _ ->
//                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    if(!etSearch.text.isNullOrEmpty()) {
//                        viewModel.search("${etSearch.text}")
//                    }
//                    return@OnEditorActionListener true
//                }
//                false
//            })
        }

        subscribeToObservables()
    }

    private fun subscribeToObservables() = with(binding) {
        viewModel.search.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Loading -> {
                    progressBar.visibility = VISIBLE
                    searchAdapter.clearAdapter()
                }
                is Resource.Success -> {
                    progressBar.visibility = GONE
                    if(!state.data.isNullOrEmpty()) {
                        searchAdapter.addAll(state.data as ArrayList<SearchModel>, true)
                    }
                }
                is Resource.Error -> {
                    progressBar.visibility = GONE
                    context.toast(state.message.ifEmpty { "Error ${state.code}" })
                }
            }
        }
    }

    override fun onSearchListener(binding: ItemSearchBinding, position: Int, data: SearchModel) {
        findNavController().navigate(R.id.action_search_to_detail)
    }
}