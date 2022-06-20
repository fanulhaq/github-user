package com.fanulhaq.githubuser

import com.fanulhaq.githubuser.ext.countDateUpdate
import com.fanulhaq.githubuser.ext.numberShortFormatter
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun number_short_formatter() {
        val data = listOf(
            100L, 1000L, 10000L, 100000L, 1000000L, 100000000L, 1000000000L
        )
        val expected = listOf(
            "100", "1K", "10K", "100K", "1M", "100M", "1B"
        )

        for(i in data.indices) {
            assertEquals(expected[i], data[i].numberShortFormatter())
        }
    }

    @Test
    fun count_date_update() {
        val data = listOf(
            "2021-01-07T22:22:28Z", "2020-01-07T22:22:28Z",
            "2022-05-07T22:22:28Z", "2022-01-07T22:22:28Z",
            "2022-06-18T22:22:28Z", "2022-06-10T22:22:28Z"
        )
        val expected = listOf(
            "1 year ago", "2 years ago",
            "1 month ago", "5 months ago",
            "1 day ago", "9 days ago"
        )

        for(i in data.indices) {
            assertEquals(expected[i], data[i].countDateUpdate())
        }
    }
}