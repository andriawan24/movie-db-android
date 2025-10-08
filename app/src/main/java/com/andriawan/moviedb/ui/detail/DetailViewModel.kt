package com.andriawan.moviedb.ui.detail

import androidx.lifecycle.ViewModel
import com.andriawan.moviedb.domain.usecases.GetMovieCreditsUseCase
import com.andriawan.moviedb.domain.usecases.GetMovieDetailUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase
) : ViewModel() {

    fun getMovieDetail(id: Int) = getMovieDetailUseCase.invoke(id)
    fun getMovieCredits(id: Int) = getMovieCreditsUseCase.invoke(id)
}