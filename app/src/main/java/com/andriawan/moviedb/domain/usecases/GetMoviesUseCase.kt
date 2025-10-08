package com.andriawan.moviedb.domain.usecases

import androidx.paging.PagingData
import com.andriawan.moviedb.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface GetMoviesUseCase {
    operator fun invoke(query: String?): Flow<PagingData<Movie>>
}