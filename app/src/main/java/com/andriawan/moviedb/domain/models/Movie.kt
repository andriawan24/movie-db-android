package com.andriawan.moviedb.domain.models

data class Movie(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val backdropPath: String?,
    val genreIds: List<Int>,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String?,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)