package com.example.weatherapp.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weatherapp.R
import com.example.weatherapp.data.local.ImageDataForCondition
import com.example.weatherapp.data.local.LocalFunctions
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.viewmodel.WeatherUiState
import com.example.weatherapp.viewmodel.WeatherViewModel

@Composable
fun ForecastReportScreen(viewModel: WeatherViewModel, onBackPressed: () -> Unit) {
    val state by viewModel.state

    ForecastReportScreenUI(
        state = state,
        onBackPressed = onBackPressed
    )
}

@Preview
@Composable
fun ForecastReportScreenPreview() {
    ForecastReportScreenUI(
        state = WeatherUiState.Success(
            weather = WeatherResponse()
        ),
        onBackPressed = {}
    )
}

@Composable
fun ForecastReportScreenUI(state: WeatherUiState, onBackPressed: () -> Unit) {
    var weather by remember { mutableStateOf(WeatherResponse()) }
    var currentTime by remember { mutableStateOf("") }
    val localFunctions = LocalFunctions()

    when (state) {
        is WeatherUiState.Success -> {
            weather = state.weather
            currentTime = localFunctions.formatDate(weather.current.last_updated)
        }
        is WeatherUiState.Error -> {
            Text("Error: ${state.message}", color = Color.Red)
        }
        else -> {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF47BFDF), // Light Blue
                        Color(0xFF4A91FF)  // Darker Blue
                    )
                )
            )
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(vertical = 16.dp)) {
            Header(onBackPressed = onBackPressed)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                TodayForecast(currentTime, weather)
                Spacer(modifier = Modifier.height(16.dp))
                HourlyForecastRow(weather)
                Spacer(modifier = Modifier.height(16.dp))
                NextForecast(weather)
            }
        }
    }
}

@Composable
fun Header(onBackPressed: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 48.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onBackPressed() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Back",
                color = Color.White,
                fontSize = 20.sp
            )
        }

        Icon(
            painter = painterResource(id = R.drawable.settingpng),
            contentDescription = "Settings",
            tint = Color.White,
            modifier = Modifier.size(24.dp).clickable {}
        )
    }
}

@Composable
fun TodayForecast(currentTime: String, weather: WeatherResponse) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Today",
            color = Color.White,
            fontSize = 22.sp
        )
        Text(
            text = currentTime,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

@Composable
fun HourlyForecastRow(weather: WeatherResponse) {
    val hourlyForecast = weather.forecast.forecastday.firstOrNull()?.hour ?: emptyList()
    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(hourlyForecast) { hour ->
            HourlyForecast(hour)
        }
    }
}

@Composable
fun HourlyForecast(hour: WeatherResponse.Hour) {
    val imageResId = ImageDataForCondition().getImagePath(hour.condition.text)
    val localFunctions = LocalFunctions()
    val hourlyTime = localFunctions.formatTime(hour.time)

    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(width = 60.dp, height = 120.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "${hour.temp_c.toInt()}°C",
                color = Color.White,
                fontSize = 16.sp
            )
            imageResId?.let {
                Image(
                    painter = painterResource(it),
                    contentDescription = "Condition Icon",
                    modifier = Modifier.size(40.dp)
                )
            }
            Text(
                text = hourlyTime,
                color = Color.White,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun NextForecast(weather: WeatherResponse) {
    val dailyForecast = weather.forecast.forecastday

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Next Forecast",
                color = Color.White,
                fontSize = 22.sp
            )

            Icon(
                painter = painterResource(id = R.drawable.canlender),
                contentDescription = "Calendar",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        LazyColumn {
            items(dailyForecast) { forecast ->
                NextForecastItem(forecast)
            }
        }
    }
}

@Composable
fun NextForecastItem(forecast: WeatherResponse.ForecastDay) {
    val imageResId = ImageDataForCondition().getImagePath(forecast.day.condition.text)
    val localFunctions = LocalFunctions()
    val date = localFunctions.formatDate("${forecast.date} 00:00")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = date,
            color = Color.White,
            fontSize = 16.sp
        )
        imageResId?.let {
            Image(
                painter = painterResource(it),
                contentDescription = "Weather Icon",
                modifier = Modifier.size(40.dp)
            )
        }
        Text(
            text = "${forecast.day.avgtemp_c.toInt()}°C",
            color = Color.White,
            fontSize = 16.sp
        )
    }
}
