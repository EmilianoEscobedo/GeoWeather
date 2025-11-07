package com.istea.geoweather.page.weather

import com.istea.geoweather.entity.Forecast
// TODO
// If I mark a city as favorite, the "isFavorite" state should change to true 
// and the city should be added to the list of favorite cities.
// If the weather data of a city comes from the favorite cities list, 
// then "isFavorite" must be true.
//
// By default, "forecastButton" should be false. 
// If the user changes it to true, the forecast weather list should be displayed.
data class WeatherState (
    val isFavorite: Boolean = false,
    val city: String,
    val temperature: String,
    val humidity: String,
    val tempMin: Double,
    val tempMax: Double,
    val dateTime: Long,
    val forecastButton: Boolean = false,
    val forecastWeatherList: List<Forecast>
)
