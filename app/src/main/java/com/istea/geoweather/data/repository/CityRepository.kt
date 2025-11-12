package com.istea.geoweather.data.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.City

class CityRepository(private val service: OpenWeatherService) {
    companion object {
        private const val LOG_TAG = "CityRepository"
    }
    private val favoriteCities = mutableListOf<City>()


    suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: cityName=$cityName, limit=$limit")
            val response = service.getCityInfoByName(cityName, limit)
            return response.map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: lat=$lat, lon=$lon, limit=$limit")
            val response = service.getCityInfoByCoordinates(lat, lon, limit)
            return response.map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }
    fun addFavoriteCity(city: City) {
        if (favoriteCities.none { it.name == city.name }) {
            favoriteCities.add(city)
            Log.d(LOG_TAG, "City added to favorites: ${city.name}")
        }
    }

    fun removeFavoriteCity(cityName: String) {
        favoriteCities.removeAll { it.name == cityName }
        Log.d(LOG_TAG, "City removed from favorites: $cityName")
    }

    fun getFavoriteCities(): List<City> = favoriteCities.toList()

    fun isCityFavorite(cityName: String): Boolean =
        favoriteCities.any { it.name == cityName }
}