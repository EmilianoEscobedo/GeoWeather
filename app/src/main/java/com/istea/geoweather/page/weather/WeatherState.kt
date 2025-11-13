package com.istea.geoweather.page.weather

import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.entity.Weather

// TODO
// If I mark a city as favorite, the "isFavorite" state should change to true 
// and the city should be added to the list of favorite cities.
// If the weather data of a city comes from the favorite cities list, 
// then "isFavorite" must be true.
//
// By default, "forecastButton" should be false. 
// If the user changes it to true, the forecast weather list should be displayed.
data class WeatherState (
    val isLoading: Boolean = false,
    val error: String? = null,
    val cityName: String = "",
    val country: String = "",
    val currentWeather: Weather? = null,
    val forecast: Forecast? = null,
    val isFavorite: Boolean = false,
)
