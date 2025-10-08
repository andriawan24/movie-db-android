package com.andriawan.moviedb.domain.models

data class Credits(
    val cast: List<Cast> = emptyList(),
    val id: Int = 0
)