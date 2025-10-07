package com.andriawan.moviedb.di

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.repository.MovieRepository
import com.andriawan.moviedb.data.repository.MovieRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun providesMovieRepository(api: MovieAPI): MovieRepository = MovieRepositoryImpl(api)
}