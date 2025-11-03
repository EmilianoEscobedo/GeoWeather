package com.istea.geoweather.data.api

import com.istea.geoweather.data.dto.CityResponseDto
import com.istea.geoweather.data.dto.ForecastResponseDto
import com.istea.geoweather.data.dto.WeatherResponseDto
import com.istea.geoweather.data.util.ApiConstants
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("geo/1.0/direct")
    suspend fun getCityInfoByName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int
    ): List<CityResponseDto>

    @GET("geo/1.0/reverse")
    suspend fun getCityInfoByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int
    ): List<CityResponseDto>

    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String = ApiConstants.UNITS_METRIC,
        @Query("lang") lang: String = ApiConstants.LANG_EN
    ): WeatherResponseDto

    @GET("data/2.5/forecast")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int,
        @Query("units") units: String = ApiConstants.UNITS_METRIC,
        @Query("lang") lang: String = ApiConstants.LANG_EN
    ): ForecastResponseDto
}
