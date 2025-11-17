package com.istea.geoweather.repository

import com.istea.geoweather.data.api.OpenWeatherClient

object RepositoryProvider {
    private val openWeatherService = OpenWeatherClient.api

    val cityRepository: CityRepository by lazy {
        CityRepository(service = openWeatherService)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepository(service = openWeatherService)
    }

    val forecastRepository: ForecastRepository by lazy {
        ForecastRepository(service = openWeatherService)
    }
}