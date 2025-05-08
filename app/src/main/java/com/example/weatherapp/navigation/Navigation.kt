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
fun Navigation(viewModel: WeatherViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.WeatherScreen.route,
        route = "main_graph"
    ) {

        composable(Screen.WeatherScreen.route) {
            WeatherScreen(
                viewModel,
                onSearchPressed = { navController.navigate(Screen.SearchScreen.route) },
                onForecastReportPressed = {navController.navigate(Screen.ForecastReportScreen.route)})
        }
        composable(Screen.SearchScreen.route) {
            SearchScreen(viewModel,
                onSearchExited = { navController.navigate(Screen.WeatherScreen.route) },
                onDataFetched = { navController.navigate(Screen.WeatherScreen.route) })
        }
        composable(Screen.ForecastReportScreen.route) {
            ForecastReportScreen(viewModel,
                onBackPressed = {navController.navigate(Screen.WeatherScreen.route)})
        }
    }
}
