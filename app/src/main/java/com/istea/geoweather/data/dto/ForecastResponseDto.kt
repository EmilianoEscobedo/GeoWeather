package com.istea.geoweather.data.dto

import com.google.gson.annotations.SerializedName

data class ForecastResponseDto(
    @SerializedName("cod") val cod: String,
    @SerializedName("message") val message: Int,
    @SerializedName("cnt") val count: Int,
    @SerializedName("list") val list: List<ForecastItemDto>,
    @SerializedName("city") val city: CityForecastDto
)

data class ForecastItemDto(
    @SerializedName("dt") val dt: Long,
    @SerializedName("main") val main: MainDto,
    @SerializedName("weather") val weather: List<WeatherConditionDto>,
    @SerializedName("clouds") val clouds: CloudsDto,
    @SerializedName("wind") val wind: WindDto,
    @SerializedName("visibility") val visibility: Int,
    @SerializedName("pop") val pop: Double,
    @SerializedName("sys") val sys: SysDto,
    @SerializedName("dt_txt") val dtTxt: String
)

data class CityForecastDto(
    @SerializedName("id") val id: Long,
    @SerializedName("name") val name: String,
    @SerializedName("coord") val coord: CoordDto,
    @SerializedName("country") val country: String,
    @SerializedName("population") val population: Long,
    @SerializedName("timezone") val timezone: Int,
    @SerializedName("sunrise") val sunrise: Long,
    @SerializedName("sunset") val sunset: Long
)

data class MainDto(
    @SerializedName("temp") val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    @SerializedName("temp_min") val tempMin: Double,
    @SerializedName("temp_max") val tempMax: Double,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("sea_level") val seaLevel: Int,
    @SerializedName("grnd_level") val grndLevel: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("temp_kf") val tempKf: Double
)
data class CoordDto(
    @SerializedName("lat") val lat: Double,
    @SerializedName("lon") val lon: Double
)

data class SysDto(
    @SerializedName("pod") val pod: String
)