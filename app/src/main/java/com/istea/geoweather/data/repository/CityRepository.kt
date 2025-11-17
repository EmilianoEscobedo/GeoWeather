package com.istea.geoweather.data.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.City

class CityRepository(private val service: OpenWeatherService) {
    companion object {
        private const val LOG_TAG = "CityRepository"
    }

    private val favoriteCities = mutableSetOf<City>()
    private var selectedCity: City? = null

    suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: cityName=$cityName, limit=$limit")
            return service.getCityInfoByName(cityName, limit).map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: lat=$lat, lon=$lon, limit=$limit")
            return service.getCityInfoByCoordinates(lat, lon, limit).map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    fun setSelectedCity(city: City) {
        selectedCity = city
    }

    fun getSelectedCity(): City? = selectedCity

    fun addFavoriteCity(city: City) {
        favoriteCities.add(city)
        Log.d(LOG_TAG, "City added to favorites: ${city.name}")
    }

    fun removeFavoriteCity(city: City) {
        favoriteCities.remove(city)
        Log.d(LOG_TAG, "City removed from favorites: ${city.name}")
    }

    fun getFavoriteCities(): List<City> {
        return favoriteCities.toList()
    }

    fun isCityFavorite(city: City): Boolean {
        return favoriteCities.contains(city)
    }
}