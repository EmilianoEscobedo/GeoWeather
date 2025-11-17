package com.istea.geoweather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.istea.geoweather.page.city.CityPage
import com.istea.geoweather.page.forecast.ForecastPage
import com.istea.geoweather.page.home.HomePage
import com.istea.geoweather.ui.theme.GeoWeatherTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GeoWeatherTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "home",
                ) {
                    composable("home") {
                        HomePage(navController = navController)
                    }
                    composable("city") {
                        CityPage(navController = navController)
                    }
                    composable("forecast") {
                        ForecastPage(navController = navController)
                    }
                }
            }
        }
    }
}