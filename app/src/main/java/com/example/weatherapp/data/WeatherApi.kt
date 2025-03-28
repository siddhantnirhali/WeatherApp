package com.example.weatherapp.data

import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

// API Interface
interface WeatherApi {
    @GET("/v1/forecast.json")
    suspend fun getWeather(
        @Query("key") apiKey: String,
        @Query("q") city: String,
        @Query("days") days: String

    ): Response<WeatherResponse>
}