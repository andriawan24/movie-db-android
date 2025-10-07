package com.andriawan.moviedb.domain.models

import com.andriawan.moviedb.data.responses.PaginationMovieResponse

data class PaginationMovie(
    val page: Int,
    val results: List<Movie>,
    val totalPages: Int,
    val totalResults: Int
) {
    companion object {
        fun from(response: PaginationMovieResponse): PaginationMovie {
            return PaginationMovie(
                page = response.page ?: 0,
                results = response.results?.map { Movie.from(it) } ?: emptyList(),
                totalPages = response.totalPages ?: 0,
                totalResults = response.totalResults ?: 0
            )
        }
    }
}