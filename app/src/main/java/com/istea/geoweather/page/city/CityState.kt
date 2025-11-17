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
    val flag: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val isLoading: Boolean = false,
    val isSearching: Boolean = false,
    val isLoadingGeolocation: Boolean = false,
    val currentCityWeather: Weather? = null,
    val selectedCity: City? = null,
    val showNoResults: Boolean = false
)