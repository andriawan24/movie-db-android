package com.andriawan.moviedb.utils.result

import okio.IOException

sealed class MovieError(open val message: String) {
    data class NetworkError(override val message: String) : MovieError(message)
    data object Unauthorized : MovieError("Unauthorized access")
    data class Unknown(override val message: String) : MovieError(message)

    companion object {
        fun from(exception: Exception): MovieError {
            return when {
                exception.message?.contains("404") == true -> Unknown("Resource not found")
                exception.message?.contains("401") == true -> Unauthorized
                exception.message?.contains("network", ignoreCase = true) == true ->
                    NetworkError(exception.message ?: "Network error occurred")

                exception is IOException -> NetworkError("No internet connection")

                else -> Unknown(exception.message ?: "Unknown error occurred")
            }
        }
    }
}