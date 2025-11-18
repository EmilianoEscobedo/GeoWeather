package com.istea.geoweather.presentation.city

import android.Manifest
import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.LocationServices
import com.istea.geoweather.repository.RepositoryProvider
import com.istea.geoweather.router.Router

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CityPage(
    router: Router,
) {
    val context = LocalContext.current
    val factory = CityViewModelFactory(
        cityRepository = RepositoryProvider.cityRepository,
        weatherRepository = RepositoryProvider.weatherRepository,
        router = router
    )
    val viewModel: CityViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()

    val locationPermissionsState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )

    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    LaunchedEffect(Unit) {
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
}