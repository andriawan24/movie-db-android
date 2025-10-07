package com.andriawan.moviedb.data.network

import com.andriawan.moviedb.data.responses.PaginationMovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieAPI {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false
    ): PaginationMovieResponse

    @GET("search/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false
    ): PaginationMovieResponse
}