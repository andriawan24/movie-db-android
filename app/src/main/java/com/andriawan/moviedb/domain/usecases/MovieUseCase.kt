package com.andriawan.moviedb.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.domain.models.PaginationMovie
import kotlinx.coroutines.flow.Flow

class MovieUseCase(private val movieRepository: MovieRepository) {
    fun getMovies(query: String?): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieRepository, query)
        }.flow
    }
}