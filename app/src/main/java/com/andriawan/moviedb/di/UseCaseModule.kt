package com.andriawan.moviedb.di

import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.domain.usecases.MovieUseCase
import com.andriawan.moviedb.domain.usecases.MovieUseCaseImpl
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
    fun providesGetMovieUseCase(movieRepository: MovieRepository): MovieUseCase =
        MovieUseCaseImpl(movieRepository)
}