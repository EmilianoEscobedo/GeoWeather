package com.istea.geoweather.data.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("coord") val coord: CoordDto,
    @SerializedName("weather") val weather: List<WeatherConditionDto>,
    @SerializedName("base") val base: String?,
    @SerializedName("main") val main: WeatherMainDto,
    @SerializedName("visibility") val visibility: Int?,
    @SerializedName("wind") val wind: WindDto?,
    @SerializedName("clouds") val clouds: CloudsDto?,
    @SerializedName("dt") val dt: Long,
    @SerializedName("sys") val sys: WeatherSysDto?,
    @SerializedName("timezone") val timezone: Int?,
    @SerializedName("id") val id: Long?,
    @SerializedName("name") val name: String?,
    @SerializedName("cod") val cod: Int?
)

data class WeatherMainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("sea_level") val seaLevel: Int? = null,
    @SerializedName("grnd_level") val grndLevel: Int? = null
)

data class WeatherConditionDto(
    @SerializedName("id") val id: Int,
    @SerializedName("main") val main: String,
    @SerializedName("description") val description: String,
    @SerializedName("icon") val icon: String
)

data class WindDto(
    @SerializedName("speed") val speed: Double,
    @SerializedName("deg") val deg: Int,
    @SerializedName("gust") val gust: Double
)

data class CloudsDto(
    @SerializedName("all") val all: Int
)

data class WeatherSysDto(
    @SerializedName("type") val type: Int?,
    @SerializedName("id") val id: Int?,
    @SerializedName("country") val country: String?,
    @SerializedName("sunrise") val sunrise: Long?,
    @SerializedName("sunset") val sunset: Long?
)