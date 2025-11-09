package com.istea.geoweather.entity

data class Weather(
    val cityName: String,
    val country: String,
    val temperature: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val description: String,
    val iconUrl: String,
    val windSpeed: Double,
    val windDeg: Int,
    val cloudiness: Int,
    val sunrise: Long,
    val sunset: Long,
    val dateTime: Long
)
