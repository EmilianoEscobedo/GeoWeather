package com.istea.geoweather.mocks.repository

import com.istea.geoweather.entity.CityForecast
import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.repository.ForecastRepository

class ForecastRepositoryMock : ForecastRepository {
    val testForecast = Forecast(
        city = CityForecast(1L, "Buenos Aires", -34.6118, -58.3960, "Argentina"),
        items = emptyList()
    )

    override suspend fun getForecast(lat: Double, lon: Double, limit: Int): Forecast = testForecast
}