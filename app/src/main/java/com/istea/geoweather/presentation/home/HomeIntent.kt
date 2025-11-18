package com.istea.geoweather.presentation.home

sealed class HomeIntent {
    object NavigateToCity : HomeIntent()
    object FinishLoading : HomeIntent()
    object OpenAboutDialog : HomeIntent()
    object CloseAboutDialog : HomeIntent()
}