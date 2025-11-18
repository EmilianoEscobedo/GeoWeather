package com.istea.geoweather.data.mapper

import com.istea.geoweather.data.dto.CityForecastDto
import com.istea.geoweather.data.dto.ForecastItemDto
import com.istea.geoweather.data.dto.ForecastResponseDto
import com.istea.geoweather.data.util.ApiConstants
import com.istea.geoweather.data.util.ApiConstants.DAILY_FORECAST_LIMIT
import com.istea.geoweather.entity.CityForecast
import com.istea.geoweather.entity.Forecast
import com.istea.geoweather.entity.ForecastItem
import com.istea.geoweather.entity.DailyForecast
import java.text.SimpleDateFormat
import java.util.*

fun ForecastResponseDto.toEntity(): Forecast {
    val domainCity = city.toEntity()
    val domainItems = list.map { it.toEntity() }
    return Forecast(city = domainCity, items = domainItems)
}

fun CityForecastDto.toEntity(): CityForecast {
    return CityForecast(
        id = id,
        name = name,
        latitude = coord.lat,
        longitude = coord.lon,
        country = country
    )
}

fun ForecastItemDto.toEntity(): ForecastItem {
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

fun List<ForecastItem>.toDailyForecasts(): List<DailyForecast> {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val displayDateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())
    val dayNameFormat = SimpleDateFormat("EEEE", Locale.getDefault())

    return this
        .groupBy { item ->
            val date = Date(item.dateTime * 1000)
            dateFormat.format(date)
        }
        .map { (_, items) ->
            val firstItem = items.first()
            val date = Date(firstItem.dateTime * 1000)
            val maxTemp = items.maxOf { it.tempMax }
            val minTemp = items.minOf { it.tempMin }
            val avgHumidity = items.map { it.humidity }.average().toInt()
            val avgWindSpeed = items.map { it.windSpeed }.average()

            val mostCommonWeather = items
                .groupBy { it.description }
                .maxByOrNull { it.value.size }
                ?.value?.first()

            DailyForecast(
                date = displayDateFormat.format(date),
                dayName = dayNameFormat.format(date),
                icon = mostCommonWeather?.icon ?: firstItem.icon,
                description = mostCommonWeather?.description ?: firstItem.description,
                tempMax = maxTemp,
                tempMin = minTemp,
                humidity = avgHumidity,
                windSpeed = avgWindSpeed
            )
        }
        .take(DAILY_FORECAST_LIMIT)
}