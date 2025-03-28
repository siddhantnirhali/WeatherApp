package com.example.weatherapp.navigation

sealed class Screen(val route: String) {
    object WeatherScreen : Screen("weather_screen")
    object SearchScreen : Screen("serach_screen")
    object ForecastReportScreen : Screen("forecast_screen")
}