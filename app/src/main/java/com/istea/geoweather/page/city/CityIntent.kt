package com.istea.geoweather.page.city

sealed class CityIntent {
    object GetDevicePosition : CityIntent()
    data class SearchCity(val text: String) : CityIntent()
    data class ShowWeather(val text: String) : CityIntent()
    object FinishLoading : CityIntent()
}
