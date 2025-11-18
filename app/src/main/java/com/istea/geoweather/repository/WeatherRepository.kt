package com.istea.geoweather.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.Weather

interface WeatherRepository {
    suspend fun getCurrentWeather(lat: Double, lon: Double): Weather
}

class WeatherRepositoryImpl(private val service: OpenWeatherService) : WeatherRepository {
    companion object {
        private const val LOG_TAG = "WeatherRepository"
    }

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Weather {
        try {
            Log.d(LOG_TAG, "Fetching current weather for: lat=$lat, lon=$lon")
            val response = service.getCurrentWeather(lat, lon)
            return response.toEntity()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching current weather", e)
            throw e
        }
    }
}