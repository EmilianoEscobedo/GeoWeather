package com.istea.geoweather.data.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toDomain
import com.istea.geoweather.entity.City

class CityRepository(private val service: OpenWeatherService) {
    companion object {
        private const val LOG_TAG = "CityRepository"
    }

    suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: cityName=$cityName, limit=$limit")
            val response = service.getCityInfoByName(cityName, limit)
            return response.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: lat=$lat, lon=$lon, limit=$limit")
            val response = service.getCityInfoByCoordinates(lat, lon, limit)
            return response.map { it.toDomain() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }
}