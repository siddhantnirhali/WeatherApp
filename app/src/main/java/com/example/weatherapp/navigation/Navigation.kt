package com.example.weatherapp.navigation

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.example.weatherapp.screens.ForecastReportScreen
import com.example.weatherapp.screens.SearchScreen
import com.example.weatherapp.screens.WeatherScreen
import com.example.weatherapp.viewmodel.WeatherViewModel


@Composable
fun Navigation(startDestination: Screen, viewModel: WeatherViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = startDestination.toString(),
        route = "main_graph"
    ) {

        composable(Screen.WeatherScreen.toString()) {
            WeatherScreen(
                viewModel,
                onSearchPressed = { navController.navigate(Screen.SearchScreen.toString()) },
                onForecastReportPressed = {navController.navigate(Screen.ForecastReportScreen.toString())})
        }
        composable(Screen.SearchScreen.toString()) {
            SearchScreen(viewModel,
                onSearchExited = { navController.navigate(Screen.WeatherScreen.toString()) },
                onDataFetched = { navController.navigate(Screen.WeatherScreen.toString()) })
        }
        composable(Screen.ForecastReportScreen.toString()) {
            ForecastReportScreen(viewModel,
                onBackPressed = {navController.navigate(Screen.WeatherScreen.toString())})
        }
    }
}
