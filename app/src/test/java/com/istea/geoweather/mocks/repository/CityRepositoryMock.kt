package com.istea.geoweather.mocks.repository

import com.istea.geoweather.entity.City
import com.istea.geoweather.repository.CityRepository

class CityRepositoryMock : CityRepository {
    val buenosAires = City(
        name = "Buenos Aires",
        latitude = -34.6118,
        longitude = -58.3960,
        country = "Argentina",
        state = "Buenos Aires",
        flag = "🇦🇷"
    )

    val cordoba = City(
        name = "Cordoba",
        latitude = -31.4201,
        longitude = -64.1888,
        country = "Argentina",
        state = "Cordoba",
        flag = "🇦🇷"
    )

    private var selectedCity: City? = buenosAires
    private val favoriteCities = mutableSetOf<City>()

    override suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> {
        return when {
            cityName.contains("cor", ignoreCase = true) -> listOf(cordoba)
            cityName.contains("buenos", ignoreCase = true) -> listOf(buenosAires)
            else -> emptyList()
        }
    }

    override suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> {
        return listOf(buenosAires)
    }

    override fun setSelectedCity(city: City) {
        selectedCity = city
    }

    override fun getSelectedCity(): City? = selectedCity

    override fun addFavoriteCity(city: City) {
        favoriteCities.add(city)
    }

    override fun removeFavoriteCity(city: City) {
        favoriteCities.remove(city)
    }

    override fun getFavoriteCities(): List<City> {
        return favoriteCities.toList()
    }

    override fun isCityFavorite(city: City): Boolean {
        return favoriteCities.contains(city)
    }
}

class CityRepositoryMockForecast : CityRepository {
    val testCity = City(
        name = "Buenos Aires",
        latitude = -34.6118,
        longitude = -58.3960,
        country = "Argentina",
        state = "Buenos Aires",
        flag = "🇦🇷"
    )

    private var selectedCity: City? = testCity
    private val favoriteCities = mutableSetOf<City>()

    override suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> = listOf(testCity)
    override suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> = listOf(testCity)
    override fun setSelectedCity(city: City) { selectedCity = city }
    override fun getSelectedCity(): City? = selectedCity
    override fun addFavoriteCity(city: City) { favoriteCities.add(city) }
    override fun removeFavoriteCity(city: City) { favoriteCities.remove(city) }
    override fun getFavoriteCities(): List<City> = favoriteCities.toList()
    override fun isCityFavorite(city: City): Boolean = favoriteCities.contains(city)
}

class CityRepositoryMockForecastNoCity : CityRepository {
    override suspend fun getCityInfoByName(cityName: String, limit: Int): List<City> = emptyList()
    override suspend fun getCityInfoByCoordinates(lat: Double, lon: Double, limit: Int): List<City> = emptyList()
    override fun setSelectedCity(city: City) {}
    override fun getSelectedCity(): City? = null
    override fun addFavoriteCity(city: City) {}
    override fun removeFavoriteCity(city: City) {}
    override fun getFavoriteCities(): List<City> = emptyList()
    override fun isCityFavorite(city: City): Boolean = false
}