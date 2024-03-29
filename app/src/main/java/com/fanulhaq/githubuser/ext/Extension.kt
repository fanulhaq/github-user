/*
 * Copyright (c) 2022 - Irfanul Haq.
 */

@file:SuppressLint("SimpleDateFormat")

package com.fanulhaq.githubuser.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

fun Context?.toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Boolean?.isTrue() = this != null && this

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

private const val k = 1000L
private const val m = 1000000L
private const val b = 1000000000L

fun Long.numberShortFormatter() : String {
    return when(this) {
        in 0..999 -> this.toString()
        in k..999999 -> {
            val x = this/k
            "${x}K"
        }
        in m..999999999 -> {
            val x = this/m
            "${x}M"
        }
        else -> {
            val x = this/b
            "${x}B"
        }
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

fun String.countDateUpdate(format: String = "yyyy-MM-dd'T'HH:mm:ss'Z'") : String {
    val now = getLocaleDateOrTimeGMT7().dateToMillis()
    val date = this.dateToMillis(format)
    val ms = now - date
    val days = TimeUnit.MILLISECONDS.toDays(ms)
    return when {
        days >= 365 -> {
            val rs = (days/365).toInt()
            "$rs year${if(rs > 1) "s" else ""} ago"
        }
        days >= 30 -> {
            val rs = (days/30).toInt()
            "$rs month${if(rs > 1) "s" else ""} ago"
        }
        days >= 1 -> {
            "$days day${if(days > 1) "s" else ""} ago"
        }
        else -> {
            val minutes = TimeUnit.MILLISECONDS.toMinutes(ms)
            when {
                minutes >= 60 -> {
                    val rs = (minutes/60).toInt()
                    "$rs hour${if(rs > 1) "s" else ""} ago"
                }
                minutes >= 1 -> {
                    "$minutes minute${if(minutes > 1) "s" else ""} ago"
                }
                else -> {
                    val seconds = TimeUnit.MILLISECONDS.toSeconds(ms)
                    "$seconds second${if(seconds > 1) "s" else ""} ago"
                }
            }
        }
    }
}