package com.andriawan.moviedb.data.network.interceptor

import com.andriawan.moviedb.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class HeaderApiKeyInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val apiKey = BuildConfig.MOVIE_API_KEY
        val request = chain.request()
        val newRequest = request.newBuilder()
            .addHeader(AUTHORIZATION_HEADER, BEARER_TOKEN + apiKey)
            .build()

        return chain.proceed(newRequest)
    }

    companion object {
        private const val AUTHORIZATION_HEADER = "Authorization"
        private const val BEARER_TOKEN = "Bearer "
    }
}