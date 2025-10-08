package com.andriawan.moviedb.domain.models

data class PaginationMovie(
    val page: Int = 0,
    val results: List<Movie> = emptyList(),
    val totalPages: Int = 0,
    val totalResults: Int = 0
)
