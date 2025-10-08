package com.andriawan.moviedb.ui.detail

import androidx.lifecycle.ViewModel
import com.andriawan.moviedb.domain.usecases.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    fun getMovieDetail(id: Int) = movieUseCase.getMovieDetail(id)
    fun getMovieCredits(id: Int) = movieUseCase.getMovieCredits(id)
}