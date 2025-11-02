package com.istea.geoweather.data.mapper

import com.istea.geoweather.data.dto.CityForecastDto
import com.istea.geoweather.data.dto.ForecastItemDto
import com.istea.geoweather.data.dto.ForecastResponseDto
import com.istea.geoweather.data.util.ApiConstants
import com.istea.geoweather.entity.CityForecast
import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.entity.ForecastItem

fun ForecastResponseDto.toDomain(): Forecast {
    val domainCity = city.toDomain()
    val domainItems = list.map { it.toDomain() }
    return Forecast(city = domainCity, items = domainItems)
}

fun CityForecastDto.toDomain(): CityForecast {
    return CityForecast(
        id = id,
        name = name,
        latitude = coord.lat,
        longitude = coord.lon,
        country = country
    )
}

fun ForecastItemDto.toDomain(): ForecastItem {
    val weatherCondition = weather.firstOrNull()

    return ForecastItem(
        dateTime = dt,
        temp = main.temp,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        feelsLike = main.feelsLike,
        pressure = main.pressure,
        humidity = main.humidity,
        windSpeed = wind.speed,
        windDeg = wind.deg,
        cloudiness = clouds.all,
        pop = pop,
        condition = weatherCondition?.main.orEmpty(),
        description = weatherCondition?.description.orEmpty(),
        icon = weatherCondition?.icon?.let { "${ApiConstants.ICON_PREFIX}$it${ApiConstants.ICON_SUFFIX}" }.orEmpty(),
        dtTxt = dtTxt
    )
}
