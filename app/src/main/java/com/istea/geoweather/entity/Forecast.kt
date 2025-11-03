package com.istea.geoweather.entity

data class Forecast(
    val city: CityForecast,
    val items: List<ForecastItem>
)

data class ForecastItem(
    val dateTime: Long,
    val temp: Double,
    val tempMin: Double,
    val tempMax: Double,
    val feelsLike: Double,
    val pressure: Int,
    val humidity: Int,
    val windSpeed: Double,
    val windDeg: Int,
    val cloudiness: Int,
    val pop: Double,
    val condition: String,
    val description: String,
    val icon: String,
    val dtTxt: String
)

data class CityForecast(
    val id: Long,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String
)
