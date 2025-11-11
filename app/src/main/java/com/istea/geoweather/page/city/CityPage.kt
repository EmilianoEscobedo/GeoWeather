package com.istea.geoweather.page.city

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.istea.geoweather.entity.City

@Composable
fun CityPage(
    navController: NavController,
) {
    var ciudades by remember { mutableStateOf(sampleCities()) }
    var favoritas by remember { mutableStateOf(emptyList<City>()) }

    val state = CityState(
        text = "",
        filterList = ciudades,
        favoriteCityList = favoritas,
        city = "",
        country = "",
        temperature = "",
    )

    CityView(
        state = state,
        onIntent = { intent ->
            when (intent) {
                is CityIntent.SearchCity -> {
                    ciudades = sampleCities().filter {
                        it.name.contains(intent.text, ignoreCase = true)
                    }
                }

                is CityIntent.ShowWeather -> {
                    navController.navigate("weather")
                }

                is CityIntent.GetDevicePosition -> {
                    // Por ahora no hace nada
                }
                is CityIntent.FinishLoading -> {
                }
            }
        },
        ciudades = ciudades,
        onBuscarCiudad = { query ->
            ciudades = sampleCities().filter { it.name.contains(query, ignoreCase = true) }
        },
        onSeleccionarCiudad = { city ->
            favoritas = favoritas + city
        },
        ciudadesFavoritas = favoritas
    )
}

fun sampleCities(): List<City> = listOf(
    City(name = "Buenos Aires", latitude = -34.6, longitude = -58.38, country = "AR"),
    City(name = "Madrid", latitude = 40.4, longitude = -3.7, country = "ES"),
    City(name = "Londres", latitude = 51.5, longitude = -0.1, country = "GB"),
    City(name = "New York", latitude = 40.7, longitude = -74.0, country = "US"),
)
