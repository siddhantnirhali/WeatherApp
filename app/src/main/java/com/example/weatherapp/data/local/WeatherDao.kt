package com.example.weatherapp.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.weatherapp.model.RecentWeatherDetails

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(weather: RecentWeatherDetails)

    @Query("SELECT * FROM RecentWeatherDetails ORDER BY id DESC")
    suspend fun getAllRecentWeather(): List<RecentWeatherDetails>

    @Query("DELETE FROM RecentWeatherDetails WHERE city = :city")
    suspend fun deleteWeatherByCity(city: String)

    @Query("SELECT COUNT(*) FROM RecentWeatherDetails")
    suspend fun getWeatherCount(): Int

    @Query("SELECT * FROM RecentWeatherDetails ORDER BY id ASC LIMIT 1")
    suspend fun getOldestWeather(): RecentWeatherDetails?

    @Transaction
    suspend fun maintainRecentWeatherEntries(weather: RecentWeatherDetails) {
        // Remove entry with the same city name if it exists
        deleteWeatherByCity(weather.city)

        // Insert the new weather entry
        insertWeather(weather)

        // Check if the count exceeds 3, and delete the oldest entry if necessary
        if (getWeatherCount() > 3) {
            val oldestWeather = getOldestWeather()
            oldestWeather?.let {
                deleteWeatherByCity(it.city)
            }
        }
    }
}
