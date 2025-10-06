package com.andriawan24.moviedb

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MovieDBApp : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
    }

    private fun initLogging() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber is initialized")
        }
    }
}