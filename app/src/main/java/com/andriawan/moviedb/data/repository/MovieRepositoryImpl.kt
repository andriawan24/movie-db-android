package com.andriawan.moviedb.data.repository

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.data.requests.GetMovieListRequest
import com.andriawan.moviedb.data.responses.CreditsResponse.Companion.toDomain
import com.andriawan.moviedb.data.responses.MovieDetailResponse.Companion.toDomain
import com.andriawan.moviedb.data.responses.PaginationMovieResponse.Companion.toDomain
import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.domain.models.PaginationMovie

class MovieRepositoryImpl(private val api: MovieAPI) : MovieRepository {

    override suspend fun getMovies(request: GetMovieListRequest): PaginationMovie {
        val response = if (request.query != null) {
            api.searchMovies(request.page, request.query)
        } else {
            api.discoverMovies(request.page)
        }

        return response.toDomain()
    }

    override suspend fun getMovieDetail(request: GetMovieDetailRequest): MovieDetail {
        val response = api.getMovieDetail(id = request.id)
        return response.toDomain()
    }

    override suspend fun getMovieCredits(request: GetMovieDetailRequest): Credits {
        val response = api.getMovieCredits(id = request.id)
        return response.toDomain()
    }
}