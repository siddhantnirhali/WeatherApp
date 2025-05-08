package com.example.weatherapp

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class WeatherApp: Application() {
    override fun onCreate() {
        super.onCreate()
        // Initialize Places SDK directly
        Places.initialize(applicationContext, BuildConfig.LOCATION_API_KEY)
    }
}