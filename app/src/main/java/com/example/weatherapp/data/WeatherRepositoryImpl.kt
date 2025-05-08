package com.example.weatherapp.data

import android.util.Log
import com.example.weatherapp.di.LocationRetrofit
import com.example.weatherapp.di.WeatherRetrofit
import com.example.weatherapp.model.GeocodingResponse
import com.example.weatherapp.model.WeatherResponse
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    @WeatherRetrofit private val weatherApi: WeatherApi,
    @LocationRetrofit private val locationApi: LocationApi
) : WeatherRepository {
    override suspend fun getWeather(key: String, city: String): Result<WeatherResponse> {
        return try {
            Log.d("FetchData", "API calling with key: $key and city: $city")
            val response = weatherApi.getWeather(key, city, "4")
            Log.d("FetchData", "Response Code: ${response.code()} - ${response.message()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("FetchData", "Success fetching data: $body")
                    Result.success(body)
                } else {
                    Log.e("FetchData", "No data in response body")
                    Result.failure(Exception("No data"))
                }
            } else {
                Log.e("FetchData", "Error response: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Error response: ${response.message()}"))
            }
        } catch (e: IOException) {
            Log.e("FetchData", "IOException occurred: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e("FetchData", "Exception occurred: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getLocation(
        lat: String,
        long: String,
        key: String
    ): Result<GeocodingResponse> {
        return try {
            val response = locationApi.getLocation("$lat,$long", key)
            Log.d("LocationData", "Fetching Location data for lat: $lat, long: $long in Repo")
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Log.d("FetchData", "Success fetching data: $body")
                    Result.success(body)
                } else {
                    Log.e("FetchData", "No data in response body")
                    Result.failure(Exception("No data"))
                }
            } else {
                Log.e("FetchData", "Error response: ${response.code()} - ${response.message()}")
                Result.failure(Exception("Error response: ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("FetchData", "Exception occurred: ${e.message}")
            Result.failure(e)
        }
    }

}
