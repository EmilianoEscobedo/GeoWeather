package com.istea.geoweather.data.mapper

import com.istea.geoweather.data.dto.CityResponseDto
import com.istea.geoweather.entity.City

fun CityResponseDto.toEntity(): City {
    return City(
        name = this.name,
        latitude = this.lat,
        longitude = this.lon,
        country = this.country,
        state = this.state,
        flag = getFlagEmoji(this.country)
    )
}

fun getFlagEmoji(countryCode: String): String {
    return countryCode.uppercase()
        .map { char -> Character.codePointAt(charArrayOf(char), 0) - 65 + 0x1F1E6 }
        .joinToString("") { code -> String(Character.toChars(code)) }
}