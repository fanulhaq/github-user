/*
 * Copyright (c) 2021 - Muchi (Irfanul Haq).
 */

package com.fanulhaq.githubuser.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fanulhaq.githubuser.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {

    var errorMessage = SingleLiveEvent<String>()

    inline fun <T> launchPagingAsync(
        crossinline execute: suspend () -> Flow<T>,
        crossinline onSuccess: (Flow<T>) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val result = execute()
                onSuccess(result)
            } catch (ex: Exception) {
                errorMessage.value = ex.message
            }
        }
    }
}