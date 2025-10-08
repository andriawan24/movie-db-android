package com.andriawan.moviedb.utils.extensions

import android.content.res.Resources

inline val Int.px get() = (this * Resources.getSystem().displayMetrics.density).toInt()

fun Int.getRuntimeFormatted(): String {
    val hour = this / 60
    val minutes = this - (hour * 60)

    return "${hour}h ${minutes}m"
}