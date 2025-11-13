package com.istea.geoweather.page.weather

sealed class WeatherIntent{
    object NavigateBack: WeatherIntent()
    object addCityToFavorite: WeatherIntent() //Add City to the list IF it not in the list and isFavorite = true
    object removeCityFromFavorite: WeatherIntent() // Remove City from the list IF it not in the list and isFavorite = false
    object retry: WeatherIntent() //it will fetch the data again from the server
}

