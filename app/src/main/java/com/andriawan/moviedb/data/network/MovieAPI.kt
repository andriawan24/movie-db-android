package com.andriawan.moviedb.data.network

import com.andriawan.moviedb.data.responses.MovieDetailResponse
import com.andriawan.moviedb.data.responses.PaginationMovieResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("discover/movie")
    suspend fun discoverMovies(
        @Query("page") page: Int,
        @Query("include_adult") includeAdult: Boolean = false
    ): PaginationMovieResponse

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("page") page: Int,
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = false
    ): PaginationMovieResponse

    @GET("movie/{id}")
    suspend fun getMovieDetail(
        @Path("id") id: Int
    ): MovieDetailResponse
}