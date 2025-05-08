package com.example.weatherapp.screens

import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.weatherapp.R
import com.example.weatherapp.model.RecentWeatherDetails
import com.example.weatherapp.model.WeatherResponse
import com.example.weatherapp.viewmodel.WeatherUiState
import com.example.weatherapp.viewmodel.WeatherViewModel
import com.google.android.libraries.places.api.Places
import fetchAutocompleteSuggestions

@Composable
fun SearchScreen(
    viewModel: WeatherViewModel,
    onSearchExited: () -> Unit,
    onDataFetched: () -> Unit
) {
    val state by viewModel.state
    val recentWeatherDetails by viewModel.recentWeatherDetails
    var city by remember { mutableStateOf("") }
    val isDataFetched by viewModel.isDataFetched
    SearchScreenUI(
        city = city,
        onCityChange = { city = it },
        state = state,
        recentWeatherDetails = recentWeatherDetails,
        onSearchExited = {
            onSearchExited() // Fetch weather for the current city
        },
        onSubmitCity = {
            viewModel.fetchWeather(city) // Trigger weather fetch on submit
            onDataFetched()
            Log.d("API Call", "Search request Successfully")
        }
    )
    LaunchedEffect(Unit) {
        viewModel.fetchRecentWeatherDetails()
    }

    // Observe isDataFetched and navigate if true
    if (isDataFetched) {
        viewModel.resetDataFetchedFlag() // Reset the flag to prevent repeated navigation
        //onDataFetched()
        Log.d("API Call", "Data Fetched Successfully")
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewSearchScreenUI() {
    SearchScreenUI(
        city = "New York",
        onCityChange = {},
        state = WeatherUiState.Success(
            weather = WeatherResponse()
        ),
        recentWeatherDetails = emptyList(),
        onSearchExited = {},
        onSubmitCity = {}
    )
}

@Composable
fun SearchScreenUI(
    city: String,
    onCityChange: (String) -> Unit,
    state: WeatherUiState,
    recentWeatherDetails: List<RecentWeatherDetails>,
    onSearchExited: () -> Unit,
    onSubmitCity: () -> Unit,

    ) {
    var isExpanded by remember { mutableStateOf(false) }
    var suggestions by remember {
        mutableStateOf<List<com.google.android.libraries.places.api.model.AutocompletePrediction>>(
            emptyList()
        )
    }
    val offsetY by animateDpAsState(
        targetValue = if (isExpanded) 0.dp else (-700).dp,
        animationSpec = tween(durationMillis = 300), label = ""
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF47BFDF),
                        Color(0xFF4A91FF)
                    )
                )
            )
    ) {
        Box(
            modifier = Modifier
                .offset(y = offsetY)
                .fillMaxWidth()
                .height(700.dp)
                .background(
                    Color.White,
                    shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp)
                )
                .align(Alignment.TopCenter)
        ) {
            Column(Modifier.padding(top = 48.dp)) {
                isExpanded = true
                SearchInputField(
                    onCityChange = onCityChange,
                    city = city,
                    onSearchExited = onSearchExited,
                    isExpanded = isExpanded,
                    onSubmitCity = onSubmitCity,
                    onSuggestionsUpdate = { updatedSuggestions ->
                        if (suggestions.isNotEmpty()) suggestions = emptyList()
                        suggestions = updatedSuggestions // Update suggestions in parent state
                    }
                )
                suggestions.forEach { suggestion ->
                    AutoCompleteSuggestions(suggestion.getFullText(null).toString(),
                        onCitySelected = { selectedCity ->
                            onCityChange(selectedCity) // Update the input field
                            onSubmitCity()// Fetch weather for the selected city
                        })
                    Log.d("Suggestion", suggestion.getFullText(null).toString())
                }
                RecentSearches(
                    recentWeatherDetails,
                    onCitySelected = { selectedCity ->
                        onCityChange(selectedCity) // Update the input field
                        onSubmitCity()// Fetch weather for the selected city
                    },
                    suggestions
                )
            }
        }
    }
}

@Composable
fun SearchInputField(
    onCityChange: (String) -> Unit,
    city: String,
    onSearchExited: () -> Unit,
    isExpanded: Boolean,
    onSubmitCity: () -> Unit,
    onSuggestionsUpdate: (List<com.google.android.libraries.places.api.model.AutocompletePrediction>) -> Unit
) {
    val context = LocalContext.current
    val placesClient = remember { Places.createClient(context) }
    var query by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .height(56.dp)
            .shadow(elevation = 2.dp, shape = RoundedCornerShape(16.dp))
            .background(
                color = Color.White,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.arrow),
                contentDescription = "Back Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onSearchExited() }
            )
            BasicTextField(
                value = city,
                onValueChange = { newValue ->
                    query = newValue
                    onCityChange(newValue)
                    // Fetch suggestions when query changes
                    if (newValue.isNotBlank()) {
                        // Fetch autocomplete suggestions and update the parent
                        fetchAutocompleteSuggestions(newValue, placesClient) { predictions ->
                            Log.d("Prediction", predictions.toString())
                            onSuggestionsUpdate(predictions) // Update suggestions in parent
                        }
                    } else {
                        onSuggestionsUpdate(emptyList()) // Clear suggestions when query is empty
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp),
                singleLine = true
            )
            Icon(
                painter = painterResource(id = R.drawable.arrowsubmit),
                contentDescription = "Submit Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable {
                        if (city.isNotBlank()) {
                            false
                            onSubmitCity()
                        }
                    }
            )
        }

    }
}


@Composable
fun RecentSearches(
    recentWeatherDetails: List<RecentWeatherDetails>,
    onCitySelected: (String) -> Unit,
    suggestions: List<com.google.android.libraries.places.api.model.AutocompletePrediction>
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Text(text = "Recent search")
        Spacer(modifier = Modifier.height(16.dp))
        recentWeatherDetails.forEach { weatherDetail ->
            RecentSearch(
                weatherDetail,
                onCitySelected = onCitySelected
            )
        }

    }
}

@Composable
fun RecentSearch(
    weatherDetail: RecentWeatherDetails,
    onCitySelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .clickable { onCitySelected(weatherDetail.city) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.clockline),
                contentDescription = "Clock Icon",
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = weatherDetail.city)
                Text(text = "${weatherDetail.temp_c}° / ${weatherDetail.temp_f}°")
            }
        }
    }
}

@Composable
fun AutoCompleteSuggestions(suggestion: String, onCitySelected: (String) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .padding(start = 48.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            modifier = Modifier
                .clickable { onCitySelected(suggestion) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = suggestion)

        }
    }
}
