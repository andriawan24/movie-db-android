package com.andriawan.moviedb.data.responses

import com.andriawan.moviedb.data.responses.MovieResponse.Companion.toDomain
import com.andriawan.moviedb.domain.models.PaginationMovie
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginationMovieResponse(
    @SerialName("page")
    val page: Int? = null,
    @SerialName("results")
    val results: List<MovieResponse>? = null,
    @SerialName("total_pages")
    val totalPages: Int? = null,
    @SerialName("total_results")
    val totalResults: Int? = null
) {
    companion object {
        fun PaginationMovieResponse.toDomain(): PaginationMovie {
            return PaginationMovie(
                page = this.page ?: 0,
                results = this.results?.map { it.toDomain() }.orEmpty(),
                totalPages = this.totalPages ?: 0,
                totalResults = this.totalResults ?: 0
            )
        }
    }
}