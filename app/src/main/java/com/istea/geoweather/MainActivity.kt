package com.istea.geoweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.istea.geoweather.presentation.city.CityPage
import com.istea.geoweather.presentation.forecast.ForecastPage
import com.istea.geoweather.presentation.home.HomePage
import com.istea.geoweather.router.Navigator
import com.istea.geoweather.router.Route
import com.istea.geoweather.ui.theme.GeoWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoWeatherTheme {
                val navController = rememberNavController()
                val router = Navigator(navController)

                NavHost(
                    navController = navController,
                    startDestination = Route.Home.id,
                ) {
                    composable(Route.Home.id) {
                        HomePage(router = router)
                    }
                    composable(Route.City.id) {
                        CityPage(router = router)
                    }
                    composable(Route.Forecast.id) {
                        ForecastPage(router = router)
                    }
                }
            }
        }
    }
}