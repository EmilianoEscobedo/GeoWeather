package com.istea.geoweather.entity

data class City(
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val country: String,
    val state: String? = null
)