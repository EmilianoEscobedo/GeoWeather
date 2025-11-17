package com.istea.geoweather.repository

import com.istea.geoweather.data.api.OpenWeatherClient

object RepositoryProvider {
    private val openWeatherService = OpenWeatherClient.api

    val cityRepository: CityRepository by lazy {
        CityRepositoryImpl(service = openWeatherService)
    }

    val weatherRepository: WeatherRepository by lazy {
        WeatherRepositoryImpl(service = openWeatherService)
    }

    val forecastRepository: ForecastRepository by lazy {
        ForecastRepositoryImpl(service = openWeatherService)
    }
}