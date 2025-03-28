package com.example.weatherapp.data

import com.example.weatherapp.model.GeocodingResponse
import com.example.weatherapp.model.WeatherResponse

// Repository
interface WeatherRepository {
    suspend fun getWeather(key: String, city: String): Result<WeatherResponse>
    suspend fun getLocation(lat: String, long: String, key: String): Result<GeocodingResponse>
}