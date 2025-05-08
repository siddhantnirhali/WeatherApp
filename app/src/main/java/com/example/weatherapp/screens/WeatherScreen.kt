package com.example.weatherapp.screens

import android.Manifest
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.local.ImageDataForCondition
import com.example.weatherapp.data.local.LocalFunctions
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.viewmodel.WeatherUiState
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    onSearchPressed: () -> Unit,
    onForecastReportPressed: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.state
    val isDataFetched by viewModel.isDataFetched
    val stateLocation by viewModel.stateLocation
    var city by remember { mutableStateOf("Pune") }
    var currentLocation by remember { mutableStateOf("") }
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    var latitude by remember { mutableStateOf("Unknown") }
    var longitude by remember { mutableStateOf("Unknown") }


    val locationPermissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
    LaunchedEffect(Unit) {
        locationPermissionState.launchMultiplePermissionRequest()
    }



    WeatherScreenUI(
        city = city,
        onCityChange = { city = it },
        state = state,
        onSearchPressed = onSearchPressed,
        onForecastReportPressed = onForecastReportPressed,
        onCurrentLocationPressed = {
            if (locationPermissionState.allPermissionsGranted) {
                getCurrentLocation(fusedLocationClient) { location ->
                    latitude = location?.latitude?.toString() ?: "Unavailable"
                    longitude = location?.longitude?.toString() ?: "Unavailable"
                    Log.d("Current Location", "$latitude And $longitude")
                    // Step 1: Fetch location first
                    viewModel.fetchLocation(latitude, longitude)
                }
            } else {
                if (locationPermissionState.shouldShowRationale) {
                    Log.d(
                        "Current Location",
                        "Location permission is required to get the current location."
                    )
                }
            }
        }
    )
}



@SuppressLint("MissingPermission")
fun getCurrentLocation(
    fusedLocationClient: FusedLocationProviderClient,
    onLocationReceived: (Location?) -> Unit
) {
    fusedLocationClient.lastLocation
        .addOnSuccessListener { location: Location? ->
            onLocationReceived(location)
        }
        .addOnFailureListener {
            onLocationReceived(null)
        }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeatherScreenUI() {
    WeatherScreenUI(
        city = "New York",
        onCityChange = {},
        state = WeatherUiState.Success(
            weather = WeatherResponse()
        ),
        onSearchPressed = {},
        onForecastReportPressed = {},
        onCurrentLocationPressed = {}
    )
}

@Composable
fun WeatherScreenUI(
    city: String,
    onCityChange: (String) -> Unit,
    state: WeatherUiState,
    onSearchPressed: () -> Unit,
    onForecastReportPressed: () -> Unit,
    onCurrentLocationPressed: () -> Unit
) {


    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF47BFDF), // Light Blue
                        Color(0xFF4A91FF)  // Deep Blue
                    )
                )
            )
    ) {

        var weather by remember {
            mutableStateOf(WeatherResponse())
        }
        if (state is WeatherUiState.Success) {weather = state.weather
            Log.d("ForecastReoprtWeatherScreen", weather.forecast.toString())
        } else WeatherResponse()
        val screenHeight = maxHeight
        val screenWidth = maxWidth
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Location Row
            LocationRow(
                city = city,
                onSearchPressed = onSearchPressed,
                onLocationClick = { onCurrentLocationPressed() })

            Spacer(modifier = Modifier.height(16.dp))

            // Weather Condition Image and Details
            WeatherConditionSection(state)

            Spacer(modifier = Modifier.height(24.dp))

            // Weather Details
            WeatherDetails(
                weather = weather,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(screenHeight * 0.4f) // Adjust height dynamically
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Forecast Report Button
            Button(
                onClick = onForecastReportPressed,
                modifier = Modifier.fillMaxWidth(0.8f) // Button adapts to screen width
            ) {
                Text(text = "Forecast Report")
            }
        }
    }
    when (state) {
        is WeatherUiState.Loading -> {
            // Show Circular Progress Indicator

            Log.d("WeatherScreen", "Data is Loading"
            )}

        is WeatherUiState.Success -> {
            onCityChange((state).weather.location.name)
            Log.d("WeatherScreen", "Data is Loaded")
        }



        else -> {}
    }
}

@Composable
fun WeatherConditionSection(state: WeatherUiState) {
    val imageData = ImageDataForCondition()
    var imageResId by remember { mutableStateOf<Int?>(null) }

    if (state is WeatherUiState.Success) {
        val weather = state.weather
        imageResId = imageData.getImagePath(weather.current.condition.text)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        imageResId?.let {
            Image(
                painter = painterResource(id = it),
                contentDescription = "Weather Condition",
                modifier = Modifier.size(150.dp) // Adjust size for responsiveness
            )
        }
    }
}

@Composable
fun WeatherDetails(weather: WeatherResponse, modifier: Modifier) {
    val localFunctions = LocalFunctions()
    val formattedDate = localFunctions.formatDate(weather.current.last_updated)

    Box(
        modifier = modifier
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.3f),
                shape = RoundedCornerShape(20.dp)
            )
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.3f),
                        Color.White.copy(alpha = 0.3f)
                    )
                ),
                shape = RoundedCornerShape(20.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Date
            Text(
                text = "Today $formattedDate",
                fontSize = 16.sp,
                color = Color.White
            )

            // Temperature and Condition
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${weather.current.temp_c.toInt()}°",
                    fontSize = 72.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = weather.current.condition.text,
                    fontSize = 20.sp,
                    color = Color.White
                )
            }

            // Wind and Humidity Details
            WeatherDetailItem(
                iconResId = R.drawable.windpng,
                label = "Wind",
                value = "${weather.current.wind_kph} km/h"
            )

            WeatherDetailItem(
                iconResId = R.drawable.humiditypng,
                label = "Hum",
                value = "${weather.current.humidity}%"
            )
        }
    }
}

@Composable
fun WeatherDetailItem(iconResId: Int, label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = iconResId),
                contentDescription = "$label Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                fontSize = 16.sp,
                color = Color.White
            )
        }
        Text(
            text = value,
            fontSize = 16.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun LocationRow(city: String, onSearchPressed: () -> Unit, onLocationClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left-aligned: City and Search Icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable {
                onSearchPressed()
            }
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                tint = Color.White,
                contentDescription = "Location Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = city,
                fontSize = 18.sp,
                color = Color.White
            )
        }

        // Right-aligned: Location Icon
        Image(
            painter = painterResource(R.drawable.locationpng),
            contentDescription = "Location Icon",
            modifier = Modifier
                .size(24.dp)
                .clickable { onLocationClick() }
        )
    }
}
