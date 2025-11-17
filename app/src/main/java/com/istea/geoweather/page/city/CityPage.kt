package com.istea.geoweather.page.city

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.istea.geoweather.data.repository.RepositoryProvider

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityPage(
    navController: NavController,
) {
    val context = LocalContext.current

    val viewModel: CityViewModel = viewModel(
        factory = CityViewModelFactory(
            cityRepository = RepositoryProvider.cityRepository,
            weatherRepository = RepositoryProvider.weatherRepository
        )
    )

    val state by viewModel.state.collectAsState()

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(navController.currentBackStackEntry) {
        viewModel.onIntent(CityIntent.RefreshFavorites)
    }

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

    CityView(
        state = state,
        onIntent = viewModel::onIntent
    )

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is CityEffect.ShowMessage -> {
                }
                CityEffect.NavigateToWeather -> {
                    navController.navigate("forecast")
                }
            }
        }
    }
}