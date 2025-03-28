package com.example.weatherapp.data.local

import com.example.weatherapp.R

class ImageDataForCondition {
    public fun getImagePath(condition: String): Int? {
        // Map parent categories to their image paths
        val categoryImageMap = mapOf(
            "Wind" to R.drawable.windpng,
            "Suncloudy" to R.drawable.suncloudypng,
            "Sun" to R.drawable.sun,
            "Storm" to R.drawable.storm,
            "Snowy" to R.drawable.snowy,
            "Severe Weather" to R.drawable.severeweather,
            "Rainy" to R.drawable.rainy,
            "Cloudy Light Rain" to R.drawable.cloudylightrain,
            "Cloudy" to R.drawable.cloudy,
            "Cloud" to R.drawable.cloud
        )

        // Map weather conditions to their parent categories
        val conditionCategoryMap = mapOf(
            "Blizzard" to "Wind",
            "Partly cloudy" to "Cloudy",
            "Partly Cloudy " to "Cloudy",
            "Sunny" to "Sun",
            "Clear" to "Cloudy",
            "Clear " to "Cloudy",
            "Cloudy " to "Cloudy",
            "Thundery outbreaks possible" to "Storm",
            "Patchy light rain with thunder" to "Storm",
            "Moderate or heavy rain with thunder" to "Storm",
            "Patchy light snow with thunder" to "Storm",
            "Moderate or heavy snow with thunder" to "Storm",
            "Patchy snow possible" to "Snowy",
            "Blowing snow" to "Snowy",
            "Patchy light snow" to "Snowy",
            "Light snow" to "Snowy",
            "Patchy moderate snow" to "Snowy",
            "Moderate snow" to "Snowy",
            "Patchy heavy snow" to "Snowy",
            "Heavy snow" to "Snowy",
            "Ice pellets" to "Snowy",
            "Light snow showers" to "Snowy",
            "Moderate or heavy snow showers" to "Snowy",
            "Light showers of ice pellets" to "Snowy",
            "Moderate or heavy showers of ice pellets" to "Snowy",
            "Moderate rain at times" to "Rainy",
            "Moderate rain" to "Rainy",
            "Heavy rain at times" to "Rainy",
            "Heavy rain" to "Rainy",
            "Moderate or heavy freezing rain" to "Rainy",
            "Moderate or heavy rain shower" to "Rainy",
            "Torrential rain shower" to "Rainy",
            "Patchy light rain" to "Cloudy Light Rain",
            "Light rain" to "Cloudy Light Rain",
            "Light rain shower" to "Cloudy Light Rain",
            "Cloudy" to "Cloudy",
            "Overcast" to "Cloudy",
            "Overcast " to "Cloudy",
            "Fog" to "Cloudy",
            "Freezing fog" to "Cloudy",
            "Mist" to "Cloud",
            "Patchy rain possible" to "Cloud",
            "Patchy rain nearby" to "Cloud",
            "Patchy sleet possible" to "Cloud",
            "Patchy freezing drizzle possible" to "Cloud",
            "Patchy light drizzle" to "Cloud",
            "Light drizzle" to "Cloud",
            "Freezing drizzle" to "Cloud",
            "Heavy freezing drizzle" to "Cloud",
            "Light freezing rain" to "Cloud",
            "Light sleet" to "Cloud",
            "Moderate or heavy sleet" to "Cloud",
            "Light sleet showers" to "Cloud",
            "Moderate or heavy sleet showers" to "Cloud"
        )

        // Get the parent category for the given condition
        val category = conditionCategoryMap[condition]

        // Return the corresponding image path, or a default path if not found
        return categoryImageMap[category]
    }

}