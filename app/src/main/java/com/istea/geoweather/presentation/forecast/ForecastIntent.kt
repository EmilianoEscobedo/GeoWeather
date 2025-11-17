package com.istea.geoweather.presentation.forecast

sealed class ForecastIntent {
    object LoadForecast : ForecastIntent()
    object ToggleFavorite : ForecastIntent()
    object ShareWeather : ForecastIntent()
    object NavigateBack : ForecastIntent()
}