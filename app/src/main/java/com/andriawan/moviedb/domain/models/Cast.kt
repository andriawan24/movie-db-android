package com.andriawan.moviedb.domain.models


data class Cast(
    val castId: Int = 0,
    val character: String = "",
    val id: Int = 0,
    val name: String = "",
    val order: Int = 0,
    val originalName: String = "",
    val popularity: Double = 0.0,
    val profilePath: String = ""
)