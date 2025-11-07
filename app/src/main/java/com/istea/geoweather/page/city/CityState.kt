package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City
import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.entity.Weather

data class CityState(
    val text: String = "",
    val filterList: List<City> = emptyList(),
    val favoriteCityList: List<City> = emptyList(),

    //wich states from the City entity we will need were?
    val city: String = "",
    val country: String = "",
    val temperature: String = "",

    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,

    val selectedCity: City? = null,
    val weather: Weather? = null,
    val forecast: Forecast? = null
    )
