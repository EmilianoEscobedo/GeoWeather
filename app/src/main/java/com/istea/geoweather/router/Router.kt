package com.istea.geoweather.router

interface Router {
    fun navigate(route: Route)
}
sealed class Route(val id: String) {
    data object Home : Route("home")
    data object City : Route("city")
    data object Forecast : Route("forecast")
}