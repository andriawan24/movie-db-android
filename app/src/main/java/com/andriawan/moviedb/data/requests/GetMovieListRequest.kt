package com.andriawan.moviedb.data.requests

data class GetMovieListRequest(
    val query: String? = null,
    val page: Int
)
