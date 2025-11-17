package com.istea.geoweather.page.forecast

import android.content.ClipData
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalClipboard
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.istea.geoweather.data.repository.RepositoryProvider

@Composable
fun ForecastPage(
    navController: NavController
) {
    val clipboard = LocalClipboard.current

    val viewModel: ForecastViewModel = viewModel(
        factory = ForecastViewModelFactory(
            cityRepository = RepositoryProvider.cityRepository,
            weatherRepository = RepositoryProvider.weatherRepository,
            forecastRepository = RepositoryProvider.forecastRepository
        )
    )
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onIntent(ForecastIntent.LoadForecast)
    }

    ForecastView(
        state = state,
        onIntent = viewModel::onIntent
    )

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ForecastEffect.ShowMessage -> {
                }
                is ForecastEffect.ShareWeatherCopied -> {
                    val city = state.city
                    val weather = state.currentWeather
                    if (city != null && weather != null) {
                        val shareText = "Weather in ${city.name}: ${weather.temperature.toInt()}°C, ${weather.description}"
                        val clipData = ClipData.newPlainText("Weather", shareText)
                        clipboard.setClipEntry(androidx.compose.ui.platform.ClipEntry(clipData))
                    }
                }
                is ForecastEffect.NavigateBack -> {
                    navController.popBackStack()
                }
            }
        }
    }
}