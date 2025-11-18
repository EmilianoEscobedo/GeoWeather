package com.istea.geoweather.mocks.repository

import com.istea.geoweather.entity.Weather
import com.istea.geoweather.repository.WeatherRepository

class WeatherRepositoryMock : WeatherRepository {
    val testWeather = Weather(
        cityName = "Buenos Aires",
        country = "Argentina",
        temperature = 25.0,
        feelsLike = 27.0,
        tempMin = 20.0,
        tempMax = 30.0,
        description = "Clear sky",
        iconUrl = "test_icon_url",
        pressure = 1013,
        humidity = 60,
        cloudiness = 10,
        windSpeed = 5.0,
        windDeg = 180,
        sunrise = 1700123400,
        sunset = 1700166600,
        dateTime = 1700145000
    )

    override suspend fun getCurrentWeather(lat: Double, lon: Double): Weather {
        return testWeather
    }
}