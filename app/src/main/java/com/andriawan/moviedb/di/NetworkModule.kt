package com.andriawan.moviedb.di

import com.andriawan.moviedb.data.network.MovieAPI
import com.andriawan.moviedb.data.network.interceptor.HeaderApiKeyInterceptor
import com.andriawan.moviedb.utils.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    fun providesOkhttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        })
        .addInterceptor(HeaderApiKeyInterceptor())
        .build()

    @Provides
    fun providesRetrofit(client: OkHttpClient): Retrofit {
        val contentTypeJson = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentTypeJson))
            .client(client)
            .build()
    }

    @Provides
    fun providesMovieApi(retrofit: Retrofit): MovieAPI {
        return retrofit.create<MovieAPI>()
    }
}