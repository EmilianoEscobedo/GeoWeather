package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City
import com.istea.geoweather.entity.Weather
data class CityState(
    val text: String = "",
    val filterList: List<City> = emptyList(),
    val favoriteCityList: List<City> = emptyList(),
    val geolocationAllowed: Boolean = true,
    val city: String = "",
    val country: String = "",
    val latitude: Double,
    val longitude: Double,
    val isLoading: Boolean = false,
    val currentCityWeather: Weather? = null
)
