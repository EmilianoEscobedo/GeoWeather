package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City

data class CityState(
    val text: String,
    val filterList: List<City>,
    val favoriteCityList: List<City>,
    val geolocationAllowed: Boolean = true,
    val city: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
    )
