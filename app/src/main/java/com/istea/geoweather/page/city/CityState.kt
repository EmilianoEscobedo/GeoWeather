package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City

data class CityState(
    val text: String = "",
    val filterList: List<City> = emptyList(),
    val favoriteCityList: List<City> = emptyList(),

    // Datos del clima actual (si hay una ciudad seleccionada)
    val city: String = "",
    val country: String = "",
    val temperature: String = "",
    val description: String = "",
    val iconUrl: String = "",
    val isLoading: Boolean = false
)
