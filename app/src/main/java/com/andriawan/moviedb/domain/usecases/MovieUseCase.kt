package com.andriawan.moviedb.domain.usecases

import androidx.paging.PagingData
import com.andriawan.moviedb.domain.models.Credits
import com.andriawan.moviedb.domain.models.Movie
import com.andriawan.moviedb.domain.models.MovieDetail
import com.andriawan.moviedb.domain.utils.ResultState
import kotlinx.coroutines.flow.Flow

interface MovieUseCase {
    fun getMovies(query: String?): Flow<PagingData<Movie>>
    fun getMovieDetail(id: Int): Flow<ResultState<MovieDetail>>
    fun getMovieCredits(id: Int): Flow<ResultState<Credits>>
}