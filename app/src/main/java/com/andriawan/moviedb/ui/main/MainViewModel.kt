package com.andriawan.moviedb.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.andriawan.moviedb.domain.usecases.MovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val movieUseCase: MovieUseCase) : ViewModel() {

    private val _query = MutableStateFlow<String?>(null)
    val query = _query.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val movies = query.flatMapLatest {
        movieUseCase.getMovies(it)
            .cachedIn(viewModelScope)
    }

    fun searchMovies(query: String) {
        viewModelScope.launch {
            _query.emit(query.ifEmpty { null })
        }
    }
}