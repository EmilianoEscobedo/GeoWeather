package com.istea.geoweather.data.mapper

import com.istea.geoweather.data.dto.CityResponseDto
import com.istea.geoweather.entity.City

fun CityResponseDto.toDomain(): City {
    return City(
        name = this.name,
        latitude = this.lat,
        longitude = this.lon,
        country = this.country,
        state = this.state
    )
}
