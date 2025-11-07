package com.istea.geoweather.page.city

sealed class CityIntent{
    object  getDevicePosition:CityIntent()
    data class searchCity(val text:String):CityIntent()
    data class showWeather(val text:String):CityIntent()
}
