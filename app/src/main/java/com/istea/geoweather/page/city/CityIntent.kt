package com.istea.geoweather.page.city

sealed class CityIntent{
    object  getGPS:CityIntent() //get the lat and long from the GPS
    data class searchCity(val text:String):CityIntent() //make a call to the API to get the city
    data class showWeather(val text:String):CityIntent() // make a call to the API to get the weather of the city and go to WeatherPage
}
