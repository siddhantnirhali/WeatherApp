package com.example.weatherapp.navigation

sealed class Screen(val route: String) {
    data object WeatherScreen : Screen("weather_screen")
    data object SearchScreen : Screen("serach_screen")
    data object ForecastReportScreen : Screen("forecast_screen")
}