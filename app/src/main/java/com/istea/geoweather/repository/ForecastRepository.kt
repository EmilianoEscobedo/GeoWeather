package com.istea.geoweather.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.Forecast

class ForecastRepository(private val service: OpenWeatherService) {
    companion object {
        private const val LOG_TAG = "ForecastRepository"
    }

    suspend fun getForecast(lat: Double, lon: Double, limit: Int): Forecast {
        try {
            Log.d(LOG_TAG, "Fetching forecast for: lat=$lat, lon=$lon, limit=$limit")
            val response = service.getForecast(lat, lon, limit)
            return response.toEntity()
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching forecast", e)
            throw e
        }
    }
}