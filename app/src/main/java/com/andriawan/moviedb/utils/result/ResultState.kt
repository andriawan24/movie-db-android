package com.andriawan.moviedb.utils.result

sealed class ResultState<out T> {
    data object Loading : ResultState<Nothing>()
    data class Success<T>(val data: T) : ResultState<T>()
    data class Error(val error: MovieError) : ResultState<Nothing>()
}