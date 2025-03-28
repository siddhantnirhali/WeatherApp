package com.example.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.local.WeatherDao
import javax.inject.Inject

class ForecastViewModel  @Inject constructor(
    private val repository: WeatherRepository,
    private val weatherDao: WeatherDao
) : ViewModel() {

}