package com.istea.geoweather.page.forecast

sealed class ForecastIntent {
    object LoadForecast : ForecastIntent()
    object ToggleFavorite : ForecastIntent()
    object ShareWeather : ForecastIntent()
    object NavigateBack : ForecastIntent()
}