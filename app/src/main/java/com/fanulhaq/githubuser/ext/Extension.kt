/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:Suppress("INTEGER_OVERFLOW")
@file:SuppressLint("SimpleDateFormat")

package com.fanulhaq.githubuser.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.*

fun Context?.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Context?.isLoadImageReady() : Boolean {
    return if(this == null) {
        false
    } else {
        if(this is Activity)
            !this.isDestroyed
        else
            true
    }
}

fun Int.numberShortFormatter() : String {
    val value = "$this"
    return when(this) {
        in 0..9999 -> value
        in 10000..999999 -> "${value.removeRange((value.lastIndex-3), value.lastIndex)}K"
        in 1000000..999999999 -> "${value.removeRange((value.lastIndex-6), value.lastIndex)}M"
        else -> "${value.removeRange((value.lastIndex-9), value.lastIndex)}B"
    }
}

fun String.formatterDate(oldFormat: String, newFormat: String) : String {
    val formatter = SimpleDateFormat(oldFormat)
    val returnFormatter = SimpleDateFormat(newFormat)

    return try {
        val date = formatter.parse(this)
        if (date == null) this else returnFormatter.format(date)
    } catch (e: ParseException) {
        e.printStackTrace()
        this
    }
}

fun getLocaleDateOrTimeGMT7(format: String = "yyyy-MM-dd") : String {
    val calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+7"), Locale.getDefault())
    val currentDate = calendar.time
    val formatter = SimpleDateFormat(format, Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("GMT+7")
    }
    return formatter.format(currentDate)
}

fun String.dateToMillis(format: String = "yyyy-MM-dd") : Long {
    val formatter = SimpleDateFormat(format)
    return try {
        val newDate = formatter.parse(this)
        newDate?.time ?: 0
    } catch (e: ParseException) {
        e.printStackTrace()
        0
    }
}

const val SECOND = 1000
const val MINUTE = 60 * SECOND
const val HOUR = 60 * MINUTE
const val DAY = 24 * HOUR
const val MONTH = 30 * DAY
const val YEAR = 12 * MONTH

fun String.countDateUpdate(format: String = "yyyy-MM-dd'T'HH:mm:ss.'Z'") : String {
    val now = getLocaleDateOrTimeGMT7().dateToMillis()
    val date = this.dateToMillis(format)
    val ms = now - date
    return when {
        ms >= YEAR -> {
            val rs = (ms/YEAR).toInt()
            "$rs year${if(rs > 1) "s" else ""} ago"
        }
        ms >= MONTH -> {
            val rs = (ms/MONTH).toInt()
            "$rs month${if(rs > 1) "s" else ""} ago"
        }
        ms >= DAY -> {
            val rs = (ms/DAY).toInt()
            "$rs day${if(rs > 1) "s" else ""} ago"
        }
        ms >= HOUR -> {
            val rs = (ms/HOUR).toInt()
            "$rs hour${if(rs > 1) "s" else ""} ago"
        }
        ms >= MINUTE -> {
            val rs = (ms/MINUTE).toInt()
            "$rs minute${if(rs > 1) "s" else ""} ago"
        }
        else -> {
            val rs = (ms/SECOND).toInt()
            "$rs second${if(rs > 1) "s" else ""} ago"
        }
    }
}