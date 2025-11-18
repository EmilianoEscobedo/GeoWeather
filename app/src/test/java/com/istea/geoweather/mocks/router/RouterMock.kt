package com.istea.geoweather.mocks.router

import com.istea.geoweather.router.Route
import com.istea.geoweather.router.Router

class RouterMock : Router {
    var lastNavigatedRoute: Route? = null
    var navigationCallCount = 0

    override fun navigate(route: Route) {
        lastNavigatedRoute = route
        navigationCallCount++
    }

    fun reset() {
        lastNavigatedRoute = null
        navigationCallCount = 0
    }
}