package com.example.weatherapp.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.weatherapp.data.LocationApi
import com.example.weatherapp.data.WeatherApi
import com.example.weatherapp.data.WeatherRepository
import com.example.weatherapp.data.WeatherRepositoryImpl
import com.example.weatherapp.data.local.AppDatabase
import com.example.weatherapp.data.local.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton

// Define Qualifiers
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LocationRetrofit
// Dependencies Module
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Provide Retrofit instance for Weather API
    @WeatherRetrofit
    @Provides
    @Singleton
    fun provideWeatherRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.weatherapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // Provide Retrofit instance for Location API
    @LocationRetrofit
    @Provides
    @Singleton
    fun provideLocationRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/") // Base URL for the Geocoding API
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideWeatherApi(@WeatherRetrofit retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Provides
    @Singleton
    fun provideLocationApi(@LocationRetrofit retrofit: Retrofit): LocationApi {
        return retrofit.create(LocationApi::class.java)
    }

    @Provides
    @Singleton
    fun provideWeatherRepository(api: WeatherApi, locationApi: LocationApi): WeatherRepository {
        return WeatherRepositoryImpl(api, locationApi)
    }


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            AppDatabase::class.java,
            "weather_database"
        )
            .addMigrations(MIGRATION_1_2) // Add the migration here
            .build()
    }

    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add the new column with a default value
            database.execSQL("ALTER TABLE RecentWeatherDetails ADD COLUMN last_updated TEXT NOT NULL DEFAULT ''")
        }
    }

    @Provides
    fun provideUserDao(database: AppDatabase): WeatherDao {
        return database.weatherDao()
    }
}