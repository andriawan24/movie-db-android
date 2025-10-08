package com.andriawan.moviedb.domain.utils

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val e: Exception) : ResultState<Nothing>()
}