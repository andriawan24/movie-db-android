package com.andriawan.moviedb.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andriawan.moviedb.data.repository.MoviePagingSource
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCaseImpl(
    private val movieRepository: MovieRepository
) : GetMoviesUseCase {
    override fun invoke(query: String?): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieRepository, query)
        }.flow
    }
}