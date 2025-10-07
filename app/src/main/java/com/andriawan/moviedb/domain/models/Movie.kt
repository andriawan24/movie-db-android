package com.andriawan.moviedb.domain.models

import com.andriawan.moviedb.data.responses.MovieResponse

data class Movie(
    val adult: Boolean = false,
    val backdropPath: String = "",
    val genreIds: List<Int> = emptyList(),
    val id: Int = 0,
    val originalLanguage: String = "",
    val originalTitle: String = "",
    val overview: String = "",
    val popularity: Double = 0.0,
    val posterPath: String = "",
    val releaseDate: String = "",
    val title: String = "",
    val video: Boolean = false,
    val voteAverage: Double = 0.0,
    val voteCount: Int = 0
) {
    companion object {
        fun from(response: MovieResponse): Movie {
            return Movie(
                adult = response.adult ?: false,
                backdropPath = response.backdropPath ?: "",
                genreIds = response.genreIds ?: emptyList(),
                id = response.id ?: 0,
                originalLanguage = response.originalLanguage ?: "",
                originalTitle = response.originalTitle ?: "",
                overview = response.overview ?: "",
                popularity = response.popularity ?: 0.0,
                posterPath = response.posterPath ?: "",
                releaseDate = response.releaseDate ?: "",
                title = response.title ?: "",
                video = response.video ?: false,
                voteAverage = response.voteAverage ?: 0.0,
                voteCount = response.voteCount ?: 0
            )
        }
    }
}