package com.istea.geoweather.data.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.City

class CityRepository(private val service: OpenWeatherService) {
    companion object {
        private const val LOG_TAG = "CityRepository"
    }

    private val persistentCities = mutableListOf<City>()
    private val favoriteCities = mutableSetOf<City>()
    private var selectedCity: City? = null

    suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: cityName=$cityName, limit=$limit")

            val existingCities = persistentCities.filter {
                it.name.trim().lowercase().contains(cityName.trim().lowercase())
            }.take(limit)

            if (existingCities.size >= limit) {
                return existingCities
            }

            val response = service.getCityInfoByName(cityName, limit)
            val apiCities = response.map { it.toEntity() }

            val newCities = mutableListOf<City>()
            apiCities.forEach { apiCity ->
                val existing = persistentCities.find {
                    it.name.trim().lowercase() == apiCity.name.trim().lowercase() &&
                            it.country.trim().lowercase() == apiCity.country.trim().lowercase()
                }

                if (existing != null) {
                    newCities.add(existing)
                } else {
                    persistentCities.add(apiCity)
                    newCities.add(apiCity)
                }
            }

            return newCities.take(limit)
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: lat=$lat, lon=$lon, limit=$limit")

            val existingCities = persistentCities.filter {
                kotlin.math.abs(it.latitude - lat) < 0.01 &&
                        kotlin.math.abs(it.longitude - lon) < 0.01
            }.take(limit)

            if (existingCities.size >= limit) {
                return existingCities
            }

            val response = service.getCityInfoByCoordinates(lat, lon, limit)
            val apiCities = response.map { it.toEntity() }

            val newCities = mutableListOf<City>()
            apiCities.forEach { apiCity ->
                val existing = persistentCities.find {
                    kotlin.math.abs(it.latitude - apiCity.latitude) < 0.01 &&
                            kotlin.math.abs(it.longitude - apiCity.longitude) < 0.01
                }

                if (existing != null) {
                    newCities.add(existing)
                } else {
                    persistentCities.add(apiCity)
                    newCities.add(apiCity)
                }
            }

            return newCities.take(limit)
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