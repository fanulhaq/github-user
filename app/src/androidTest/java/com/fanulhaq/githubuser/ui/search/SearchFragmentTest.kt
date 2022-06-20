/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

package com.fanulhaq.githubuser.ui.search

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.fanulhaq.githubuser.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@AndroidEntryPoint
@RunWith(AndroidJUnit4::class)
class SearchFragmentTest {

    private lateinit var scenario: FragmentScenario<SearchFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_AjaibAssessment)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun test_search() {
        onView(withId(R.id.etSearch)).perform(typeText("muchi"))
    }
}