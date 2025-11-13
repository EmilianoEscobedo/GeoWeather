package com.istea.geoweather.page.city

import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.istea.geoweather.entity.City

@Composable
fun CityPage(
    navController: NavController,
) {
    var cities by remember { mutableStateOf(sampleCities()) }
    var favorites by remember { mutableStateOf(emptyList<City>()) }
    var searchText by remember { mutableStateOf("") }

    val state = CityState(
        text = searchText,
        filterList = cities,
        favoriteCityList = favorites,
        city = "",
        country = "",
        latitude = 0.0,
        longitude = 0.0
    )

    CityView(
        state = state,
        onIntent = { intent ->
            when (intent) {
                is CityIntent.SearchCity -> {
                    searchText = intent.text
                    cities = sampleCities().filter {
                        it.name.contains(searchText, ignoreCase = true)
                    }
                }

                is CityIntent.ShowWeather -> {
                    navController.navigate("weather")
                }

                is CityIntent.GetDevicePosition -> {
                    // Not implemented yet
                }

                is CityIntent.FinishLoading -> {
                    // No action needed for now
                }
            }
        },
        cities = cities,
        onSearchCity = { query ->
            searchText = query
            cities = sampleCities().filter { it.name.contains(searchText, ignoreCase = true) }
        },
        onSelectCity = { city ->
            favorites = favorites + city
            // Navigate to the weather page with the selected city
            navController.navigate("weather/${city.name}")
        },
        favoriteCities = favorites
    )
}

fun sampleCities(): List<City> = listOf(
    City(name = "Buenos Aires", latitude = -34.6, longitude = -58.38, country = "AR"),
    City(name = "Madrid", latitude = 40.4, longitude = -3.7, country = "ES"),
    City(name = "London", latitude = 51.5, longitude = -0.1, country = "GB"),
    City(name = "New York", latitude = 40.7, longitude = -74.0, country = "US"),
)
