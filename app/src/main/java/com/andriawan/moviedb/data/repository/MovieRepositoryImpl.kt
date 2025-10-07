package com.andriawan.moviedb.data.repository

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.domain.models.PaginationMovie
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(private val api: MovieAPI) : MovieRepository {

    override suspend fun getMovies(request: GetMovieListRequest): PaginationMovie {
        val response = if (request.query != null) {
            api.getMovies(request.page, request.query)
        } else {
            api.getMovies(request.page)
        }

        return PaginationMovie.from(response)
    }
}