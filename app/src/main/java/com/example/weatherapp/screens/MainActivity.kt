package com.example.weatherapp.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.navigation.Navigation
import com.example.weatherapp.navigation.Screen
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navHostController = rememberNavController()
                    val viewModel = hiltViewModel<WeatherViewModel>()
                    val state = viewModel.state
                    Navigation(Screen.WeatherScreen)
                   // WeatherScreen()
                    //SearchScreen(onSearchExited = {}, onDataFetched = {})
                }
            }
        }
    }
}
// Constants
const val API_KEY = "2454812aae6243599b973140251901"

const val LOCATION_API_KEY = "AIzaSyBhytTwYT9Tq6hK6Q2enkbyVA9xPijQtrU"