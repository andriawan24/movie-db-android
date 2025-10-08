package com.andriawan.moviedb.utils.extensions

import com.andriawan.moviedb.utils.result.MovieError
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest

suspend fun <T> Flow<ResultState<T>>.observe(
    onLoading: (suspend () -> Unit)? = null,
    onSuccess: (suspend (T) -> Unit)? = null,
    onError: (suspend (MovieError) -> Unit)? = null
) {
    collectLatest { state ->
        when (state) {
            is ResultState.Loading -> onLoading?.invoke()
            is ResultState.Success -> onSuccess?.invoke(state.data)
            is ResultState.Error -> onError?.invoke(state.error)
        }
    }
}