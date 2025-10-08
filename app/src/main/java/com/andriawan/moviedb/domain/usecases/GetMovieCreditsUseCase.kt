package com.andriawan.moviedb.domain.usecases

import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.utils.result.ResultState
import kotlinx.coroutines.flow.Flow

interface GetMovieCreditsUseCase {
    operator fun invoke(movieId: Int): Flow<ResultState<Credits>>
}