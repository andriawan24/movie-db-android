package com.andriawan.moviedb.domain.usecases

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieUseCaseImpl(private val movieRepository: MovieRepository) : MovieUseCase {
    override fun getMovies(query: String?): Flow<PagingData<Movie>> {
        return Pager(PagingConfig(pageSize = 20)) {
            MoviePagingSource(movieRepository, query)
        }.flow
    }

    override fun getMovieDetail(id: Int): Flow<ResultState<MovieDetail>> = flow {
        emit(ResultState.Loading)
        try {
            val result = movieRepository.getMovieDetail(GetMovieDetailRequest(id))
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }

    override fun getMovieCredits(id: Int): Flow<ResultState<Credits>> = flow {
        emit(ResultState.Loading)
        try {
            val result = movieRepository.getMovieCredits(GetMovieDetailRequest(id))
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(e))
        }
    }
}