package com.istea.geoweather.router

import androidx.navigation.NavHostController

class Navigator(
    private val navHostController: NavHostController
) : Router {
    override fun navigate(route: Route) {
        when (route) {
            Route.Home -> navHostController.navigate(route.id)
            Route.City -> navHostController.navigate(route.id)
            Route.Forecast -> navHostController.navigate(route.id)
        }
    }
}