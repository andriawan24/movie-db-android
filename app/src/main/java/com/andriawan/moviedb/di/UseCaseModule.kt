package com.andriawan.moviedb.di

import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.domain.usecases.GetMovieCreditsUseCase
import com.andriawan.moviedb.domain.usecases.GetMovieCreditsUseCaseImpl
import com.andriawan.moviedb.domain.usecases.GetMovieDetailUseCase
import com.andriawan.moviedb.domain.usecases.GetMovieDetailUseCaseImpl
import com.andriawan.moviedb.domain.usecases.GetMoviesUseCase
import com.andriawan.moviedb.domain.usecases.GetMoviesUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun providesGetMoviesUseCase(movieRepository: MovieRepository): GetMoviesUseCase =
        GetMoviesUseCaseImpl(movieRepository)

    @Provides
    @ViewModelScoped
    fun providesGetMovieDetailUseCase(movieRepository: MovieRepository): GetMovieDetailUseCase =
        GetMovieDetailUseCaseImpl(movieRepository)

    @Provides
    @ViewModelScoped
    fun providesGetMovieCreditsUseCase(movieRepository: MovieRepository): GetMovieCreditsUseCase =
        GetMovieCreditsUseCaseImpl(movieRepository)
}