package com.andriawan.moviedb.utils.extensions

fun String.extractYear(): String {
    return this.substring(0, 4)
}