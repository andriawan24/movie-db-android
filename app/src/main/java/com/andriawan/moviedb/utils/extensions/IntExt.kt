package com.andriawan.moviedb.utils.extensions

import android.content.res.Resources

inline val Int.px get() = (this * Resources.getSystem().displayMetrics.density).toInt()