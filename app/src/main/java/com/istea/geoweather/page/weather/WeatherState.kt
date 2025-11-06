package com.istea.geoweather.page.weather

import com.istea.geoweather.entity.Forecast

data class WeatherState (
    val isFavorite: Boolean = false,
    // si lo hago favorito, tiene que cambiar a true y llevarlo al listado de favoritos de city
    // Si viene del listado de city, tiene que entrar directamente como true.
    val city: String,
    val temperature: String,
    val humidity: String,
    val tempMin: Double,
    val tempMax: Double,
    val dateTime: Long,
    val forecastButton: Boolean = false,
    //Por default siempre va a venir como false. Puedo vovlerlo true para que me muestre la lista de forecastweather
    val forecastWeatherList: List<Forecast>
)