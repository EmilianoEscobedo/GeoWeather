package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City

sealed class CityIntent{
    object  getDevicePosition:CityIntent()
    data class searchCity(val text:String):CityIntent()
    data class showWeather(val text:String):CityIntent()
    data class selectCity(val city: City) : CityIntent()
    object refresh : CityIntent()
}
