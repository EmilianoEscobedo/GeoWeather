package com.istea.geoweather.page.weather

sealed class WeatherIntent{
    object goBackToCity: WeatherIntent() //return to CityPage
    data class addToFavorite(val city: String): WeatherIntent() //Add City to the list IF it not in the list and isFavorite = true
    data class removeFromFavorite(val city: String): WeatherIntent() // Remove City from the list IF it not in the list and isFavorite = false
    data class getForescat(val city: String): WeatherIntent() //it's only show when forecastButton = true
}

