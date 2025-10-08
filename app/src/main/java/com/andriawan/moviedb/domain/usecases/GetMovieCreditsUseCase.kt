package com.andriawan.moviedb.domain.usecases

import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.requests.GetMovieDetailRequest
import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.utils.result.MovieError
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMovieCreditsUseCase @Inject constructor(private val movieRepository: MovieRepository) {
    fun invoke(movieId: Int): Flow<ResultState<Credits>> = flow {
        emit(ResultState.Loading)
        try {
            val result = movieRepository.getMovieCredits(GetMovieDetailRequest(movieId))
            emit(ResultState.Success(result))
        } catch (e: Exception) {
            emit(ResultState.Error(MovieError.from(e)))
        }
    }
}