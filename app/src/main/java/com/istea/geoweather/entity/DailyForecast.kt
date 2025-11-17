package com.istea.geoweather.entity

data class DailyForecast(
    val date: String,
    val dayName: String,
    val icon: String,
    val description: String,
    val tempMax: Double,
    val tempMin: Double,
    val humidity: Int,
    val windSpeed: Double
)