package com.andriawan.moviedb.domain.models

data class MovieDetail(
    val id: Int,
    val title: String,
    val adult: Boolean,
    val backdropPath: String?,
    val budget: Int,
    val genres: List<Genre>,
    val homepage: String?,
    val imdbId: String?,
    val originalLanguage: String,
    val originalTitle: String,
    val overview: String,
    val popularity: Double,
    val posterPath: String,
    val productionCompanies: List<ProductionCompany>,
    val productionCountries: List<ProductionCountry>,
    val releaseDate: String,
    val revenue: Int,
    val runtime: Int,
    val spokenLanguages: List<SpokenLanguage>,
    val status: String,
    val tagline: String?,
    val video: Boolean,
    val voteAverage: Double,
    val voteCount: Int
)