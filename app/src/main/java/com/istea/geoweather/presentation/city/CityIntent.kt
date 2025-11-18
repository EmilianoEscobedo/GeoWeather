package com.istea.geoweather.presentation.city

sealed class CityIntent {
    object getDevicePosition : CityIntent()
    data class searchCity(val text: String) : CityIntent()
    data class selectCity(val city: com.istea.geoweather.entity.City) : CityIntent()
    data class selectFavoriteCity(val city: com.istea.geoweather.entity.City) : CityIntent()
    object navigateToExtendedForecast : CityIntent()
    object FinishLoading : CityIntent()
    object RefreshFavorites : CityIntent()
    data class toggleFavorite(val city: com.istea.geoweather.entity.City) : CityIntent()
}