package com.andriawan.moviedb.domain.usecases

import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.flow.Flow

interface GetMovieDetailUseCase {
    operator fun invoke(movieId: Int): Flow<ResultState<MovieDetail>>
}