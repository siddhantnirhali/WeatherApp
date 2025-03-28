package com.example.weatherapp.data

import com.example.weatherapp.model.GeocodingResponse
import com.example.weatherapp.model.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {
    @GET("/maps/api/geocode/json")
    suspend fun getLocation(
        @Query("latlng") city: String,
        @Query("key") apiKey: String
    ): Response<GeocodingResponse>
}