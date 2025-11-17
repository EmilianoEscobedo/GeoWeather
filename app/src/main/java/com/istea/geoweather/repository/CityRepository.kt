package com.istea.geoweather.repository

import android.util.Log
import com.istea.geoweather.data.api.OpenWeatherService
import com.istea.geoweather.data.mapper.toEntity
import com.istea.geoweather.entity.City


interface CityRepository {
    suspend fun getCityInfoByName(cityName: String, limit: Int): List<City>
    suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City>
    fun setSelectedCity(city: City)
    fun getSelectedCity(): City?
    fun addFavoriteCity(city: City)
    fun removeFavoriteCity(city: City)
    fun getFavoriteCities(): List<City>
    fun isCityFavorite(city: City): Boolean
}

class CityRepositoryImpl(private val service: OpenWeatherService) : CityRepository {
    companion object {
        private const val LOG_TAG = "CityRepository"
    }

    private val favoriteCities = mutableSetOf<City>()
    private var selectedCity: City? = null

    override suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: cityName=$cityName, limit=$limit")
            return service.getCityInfoByName(cityName, limit).map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    override suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        try {
            Log.d(LOG_TAG, "Fetching cities for: lat=$lat, lon=$lon, limit=$limit")
            return service.getCityInfoByCoordinates(lat, lon, limit).map { it.toEntity() }
        } catch (e: Exception) {
            Log.e(LOG_TAG, "Error fetching cities", e)
            throw e
        }
    }

    override fun setSelectedCity(city: City) {
        selectedCity = city
    }

    override fun getSelectedCity(): City? = selectedCity

    override fun addFavoriteCity(city: City) {
        favoriteCities.add(city)
        Log.d(LOG_TAG, "City added to favorites: ${city.name}")
    }

    override fun removeFavoriteCity(city: City) {
        favoriteCities.remove(city)
        Log.d(LOG_TAG, "City removed from favorites: ${city.name}")
    }

    override fun getFavoriteCities(): List<City> {
        return favoriteCities.toList()
    }

    override fun isCityFavorite(city: City): Boolean {
        return favoriteCities.contains(city)
    }
}