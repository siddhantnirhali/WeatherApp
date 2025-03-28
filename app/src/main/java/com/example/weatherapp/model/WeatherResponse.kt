package com.example.weatherapp.model

import androidx.room.Entity

@Entity(tableName = "WeatherResponse")
data class WeatherResponse(
    val current: Current = Current(),
    val location: Location = Location(),
    val forecast: Forecast = Forecast()
) {
    data class Current(
        val cloud: Int = 0,
        val condition: Condition = Condition(),
        val dewpoint_c: Float = 0.0f,
        val dewpoint_f: Float = 0.0f,
        val feelslike_c: Float = 0.0f,
        val feelslike_f: Float = 0.0f,
        val gust_kph: Float = 0.0f,
        val gust_mph: Float = 0.0f,
        val heatindex_c: Float = 0.0f,
        val heatindex_f: Float = 0.0f,
        val humidity: Int = 0,
        val is_day: Int = 0,
        val last_updated: String = "",
        val last_updated_epoch: Long = 0L,
        val precip_in: Float = 0.0f,
        val precip_mm: Float = 0.0f,
        val pressure_in: Float = 0.0f,
        val pressure_mb: Float = 0.0f,
        val temp_c: Float = 0.0f,
        val temp_f: Float = 0.0f,
        val uv: Float = 0.0f,
        val vis_km: Float = 0.0f,
        val vis_miles: Float = 0.0f,
        val wind_degree: Int = 0,
        val wind_dir: String = "",
        val wind_kph: Float = 0.0f,
        val wind_mph: Float = 0.0f,
        val windchill_c: Float = 0.0f,
        val windchill_f: Float = 0.0f
    )

    data class Location(
        val country: String = "",
        val lat: Float = 0.0f,
        val localtime: String = "",
        val localtime_epoch: Long = 0L,
        val lon: Float = 0.0f,
        val name: String = "",
        val region: String = "",
        val tz_id: String = ""
    )

    data class Condition(
        val code: Int = 0,
        val icon: String = "",
        val text: String = ""
    )

    data class Forecast(
        val forecastday: List<ForecastDay> = emptyList()
    )

    data class ForecastDay(
        val date: String = "",
        val date_epoch: Long = 0L,
        val day: Day = Day(),
        val astro: Astro = Astro(),
        val hour: List<Hour> = emptyList()
    )

    data class Day(
        val maxtemp_c: Float = 0.0f,
        val maxtemp_f: Float = 0.0f,
        val mintemp_c: Float = 0.0f,
        val mintemp_f: Float = 0.0f,
        val avgtemp_c: Float = 0.0f,
        val avgtemp_f: Float = 0.0f,
        val maxwind_mph: Float = 0.0f,
        val maxwind_kph: Float = 0.0f,
        val totalprecip_mm: Float = 0.0f,
        val totalprecip_in: Float = 0.0f,
        val totalsnow_cm: Float = 0.0f,
        val avgvis_km: Float = 0.0f,
        val avgvis_miles: Float = 0.0f,
        val avghumidity: Int = 0,
        val daily_will_it_rain: Int = 0,
        val daily_chance_of_rain: Int = 0,
        val daily_will_it_snow: Int = 0,
        val daily_chance_of_snow: Int = 0,
        val condition: Condition = Condition(),
        val uv: Float = 0.0f
    )

    data class Astro(
        val sunrise: String = "",
        val sunset: String = "",
        val moonrise: String = "",
        val moonset: String = "",
        val moon_phase: String = "",
        val moon_illumination: String = "",
        val is_moon_up: Int = 0,
        val is_sun_up: Int = 0
    )

    data class Hour(
        val time_epoch: Long = 0L,
        val time: String = "",
        val temp_c: Float = 0.0f,
        val temp_f: Float = 0.0f,
        val is_day: Int = 0,
        val condition: Condition = Condition(),
        val wind_mph: Float = 0.0f,
        val wind_kph: Float = 0.0f,
        val wind_degree: Int = 0,
        val wind_dir: String = "",
        val pressure_mb: Float = 0.0f,
        val pressure_in: Float = 0.0f,
        val precip_mm: Float = 0.0f,
        val precip_in: Float = 0.0f,
        val snow_cm: Float = 0.0f,
        val humidity: Int = 0,
        val cloud: Int = 0,
        val feelslike_c: Float = 0.0f,
        val feelslike_f: Float = 0.0f,
        val windchill_c: Float = 0.0f,
        val windchill_f: Float = 0.0f,
        val heatindex_c: Float = 0.0f,
        val heatindex_f: Float = 0.0f)
}

