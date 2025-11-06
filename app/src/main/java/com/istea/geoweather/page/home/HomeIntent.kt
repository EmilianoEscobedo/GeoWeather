package com.istea.geoweather.page.home

sealed class HomeIntent {
    object NavigateToCity : HomeIntent()
    object NavigateToAbout : HomeIntent()
    object FinishLoading : HomeIntent()
}