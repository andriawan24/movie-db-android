package com.andriawan.moviedb.data.repository

import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.domain.models.PaginationMovie

interface MovieRepository {
    suspend fun getMovies(request: GetMovieListRequest): PaginationMovie
    suspend fun getMovieDetail(request: GetMovieDetailRequest): MovieDetail
}