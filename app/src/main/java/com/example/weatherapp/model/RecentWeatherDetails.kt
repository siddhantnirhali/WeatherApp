package com.example.weatherapp.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class RecentWeatherDetails(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val city : String,
    val last_updated : String,
    val temp_c : String,
    val temp_f : String
)
