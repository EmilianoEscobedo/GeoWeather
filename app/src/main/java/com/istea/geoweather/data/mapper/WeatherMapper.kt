package com.istea.geoweather.data.mapper


import com.istea.geoweather.data.dto.WeatherResponseDto
import com.istea.geoweather.data.util.ApiConstants
import com.istea.geoweather.entity.Weather

fun WeatherResponseDto.toEntity(): Weather {
    val condition = weather.firstOrNull()
    return Weather(
        cityName = name.orEmpty(),
        country = sys?.country.orEmpty(),
        temperature = main.temp,
        feelsLike = main.feelsLike,
        tempMin = main.tempMin,
        tempMax = main.tempMax,
        pressure = main.pressure,
        humidity = main.humidity,
        description = condition?.description.orEmpty(),
        iconUrl = condition?.icon?.let { "${ApiConstants.ICON_PREFIX}$it${ApiConstants.ICON_SUFFIX}" }.orEmpty(),
        windSpeed = wind?.speed ?: 0.0,
        windDeg = wind?.deg ?: 0,
        cloudiness = clouds?.all ?: 0,
        sunrise = sys?.sunrise ?: 0,
        sunset = sys?.sunset ?: 0,
        dateTime = dt
    )
}
