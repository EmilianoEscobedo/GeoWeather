package com.istea.geoweather.page.city

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.savedstate.SavedStateRegistryOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.istea.geoweather.data.remote.RetrofitClient
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.WeatherRepository
import com.istea.geoweather.entity.City

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityPage(
    navController: NavController,
) {
    val context = LocalContext.current
    val owner = context as SavedStateRegistryOwner
    val openWeatherService = RetrofitClient.service
    val cityRepository = CityRepository(service = openWeatherService)
    val weatherRepository = WeatherRepository(service = openWeatherService)
    val viewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(
            owner = owner,
            cityRepository = cityRepository,
            weatherRepository = weatherRepository
        )
    )
    val state by viewModel.state.collectAsState()

    // --- 2. LÓGICA DE PERMISOS DE UBICACIÓN ---
    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(locationPermissionsState.allPermissionsGranted) {
        if (locationPermissionsState.allPermissionsGranted) {
            @SuppressLint("MissingPermission")
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.saveLocationInHandle(location.latitude, location.longitude, true)
                    viewModel.onIntent(CityIntent.getDevicePosition)
                }
            }
        } else {
            locationPermissionsState.launchMultiplePermissionRequest()
        }
    }

    val currentCityForView = remember(state.city, state.country, state.latitude, state.longitude) {
        if (state.city.isNotEmpty()) {
            City(
                name = state.city,
                country = state.country,
                latitude = state.latitude,
                longitude = state.longitude
            )
        } else {
            null
        }
    }

    CityView(
        state = state,
        onIntent = viewModel::onIntent,
        cities = state.filterList,
        favoriteCities = state.favoriteCityList,
        currentCity = currentCityForView,
    )

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CityEffect.ShowMessage -> {
                }
                is CityEffect.NavigateToForecast -> {
                    navController.navigate("forecast")
                }
            }
        }
    }
}
