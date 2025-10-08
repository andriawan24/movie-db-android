package com.andriawan.moviedb.utils.extensions

fun String.extractYear(): String {
    return this.take(4)
}