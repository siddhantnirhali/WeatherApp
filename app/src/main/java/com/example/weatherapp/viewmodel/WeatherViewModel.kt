package com.example.weatherapp.viewmodel

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.model.GeocodingResponse
import com.example.weatherapp.model.RecentWeatherDetails
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.screens.API_KEY
import com.example.weatherapp.screens.LOCATION_API_KEY
import com.example.weatherapp.screens.WeatherDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val weatherDao: WeatherDao
) : ViewModel() {

    private val _state = mutableStateOf<WeatherUiState>(WeatherUiState.Loading)
    val state: State<WeatherUiState> = _state

    private val _stateLocation = mutableStateOf<LocationUiState>(LocationUiState.Loading)
    val stateLocation: State<LocationUiState> = _stateLocation

    private val _recentWeatherDetails = mutableStateOf<List<RecentWeatherDetails>>(emptyList())
    val recentWeatherDetails: State<List<RecentWeatherDetails>> = _recentWeatherDetails

    // Add isDataFetched mutable state
    private val _isDataFetched = mutableStateOf(false)
    val isDataFetched: State<Boolean> = _isDataFetched

    fun fetchRecentWeatherDetails() {
        viewModelScope.launch {
            _recentWeatherDetails.value = weatherDao.getAllRecentWeather()
        }
    }


    fun fetchWeather(city: String) {
        Log.d("WeatherViewModel", "Fetching weather data for city: $city")
        viewModelScope.launch {
            _state.value = WeatherUiState.Loading
            try {
                Log.d("WeatherViewModel", "Calling repository to get weather data.")
                val response = repository.getWeather(API_KEY, city)

                if (response.isSuccess) {
                    Log.d("WeatherViewModel", "Weather data fetched successfully.")
                    val weather = WeatherResponse(response.getOrThrow().current, response.getOrThrow().location)
                    Log.d("currentWeather", weather.current.temp_f.toString())
                    val weatherDetails = RecentWeatherDetails(city = weather.location.name, last_updated = weather.current.last_updated, temp_c = weather.current.temp_c.toString(), temp_f = weather.current.temp_f.toString())
                    weatherDao.maintainRecentWeatherEntries(weatherDetails)

                    _state.value = WeatherUiState.Success(weather)
                    _isDataFetched.value = true // Set flag to true when data is fetched successfully
                } else {
                    Log.e("WeatherViewModel", "Failed to fetch weather data: ${response.exceptionOrNull()?.message}")
                    _state.value = WeatherUiState.Error("Failed to fetch weather data")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Exception occurred: ${e.message}")
                _state.value = WeatherUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
    fun resetDataFetchedFlag() {
        _isDataFetched.value = false // Reset the flag after navigation
    }

    fun fetchLocation(lat: String, long: String) {
        Log.d("WeatherViewModel", "Fetching Location data for lat: $lat, long: $long")
        viewModelScope.launch {
            _stateLocation.value = LocationUiState.Loading
            try {
                Log.d("WeatherViewModel", "Calling repository to get weather data.")
                val response = repository.getLocation(lat, long, LOCATION_API_KEY)

                if (response.isSuccess) {
                    Log.d("WeatherViewModel", "Location data fetched successfully.")

                    if (response.getOrThrow().results.isNotEmpty()) {
                        _stateLocation.value = LocationUiState.Success(response.getOrThrow())
                        val city = response.getOrThrow().results[0].address_components[4].long_name.toString()
                        fetchWeather(city)
                        Log.d("LocationDetails", response.getOrThrow().results[0].address_components[4].long_name.toString())
                    } else {
                        _stateLocation.value = LocationUiState.Error("No results found")
                        Log.d("LocationDetails", "No results found")
                    }

                } else {
                    Log.e("WeatherViewModel", "Failed to fetch weather data: ${response.exceptionOrNull()?.message}")
                    _stateLocation.value = LocationUiState.Error("Failed to fetch weather data")
                }
            } catch (e: Exception) {
                Log.e("WeatherViewModel", "Exception occurred: ${e.message}")
                _stateLocation.value = LocationUiState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class WeatherUiState {
    object Loading : WeatherUiState()
    data class Success(val weather: WeatherResponse) : WeatherUiState()
    data class Error(val message: String) : WeatherUiState()
}

sealed class LocationUiState {
    object Loading : LocationUiState()
    data class Success(val data: GeocodingResponse) : LocationUiState()
    data class Error(val message: String) : LocationUiState()
}
