package com.istea.geoweather.page.weather

sealed class WeatherIntent{
    object goBackToCity: WeatherIntent()
    data class addCityToFavorite(val city: String): WeatherIntent() //Add City to the list IF it not in the list and isFavorite = true
    data class removeCityFromFavorite(val city: String): WeatherIntent() // Remove City from the list IF it not in the list and isFavorite = false
    data class getForescat(val city: String): WeatherIntent() //it's only show when forecastButton = true
}

