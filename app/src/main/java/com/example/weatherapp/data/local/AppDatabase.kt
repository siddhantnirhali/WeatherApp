package com.example.weatherapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.weatherapp.model.RecentWeatherDetails
import com.example.weatherapp.model.WeatherResponse

@Database(entities = [RecentWeatherDetails::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDao(): WeatherDao
}
