package com.istea.geoweather.page.forecast

import com.istea.geoweather.entity.City
import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.entity.Weather

data class ForecastState(
    val isLoading: Boolean = false,
    val city: City? = null,
    val currentWeather: Weather? = null,
    val forecast: Forecast? = null,
    val isFavorite: Boolean = false,
    val error: String? = null
)