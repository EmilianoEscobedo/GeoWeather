package com.istea.geoweather.presentation.forecast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.istea.geoweather.repository.CityRepository
import com.istea.geoweather.repository.ForecastRepository
import com.istea.geoweather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class ForecastEffect {
    data class ShowMessage(val text: String) : ForecastEffect()
    object ShareWeatherCopied : ForecastEffect()
    object NavigateBack : ForecastEffect()
}

class ForecastViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ForecastState())
    val state: StateFlow<ForecastState> = _state.asStateFlow()

    private val _effects = MutableSharedFlow<ForecastEffect>()
    val effects: SharedFlow<ForecastEffect> = _effects.asSharedFlow()

    fun onIntent(intent: ForecastIntent) {
        when (intent) {
            ForecastIntent.LoadForecast -> loadForecast()
            ForecastIntent.ToggleFavorite -> toggleFavorite()
            ForecastIntent.ShareWeather -> shareWeather()
            ForecastIntent.NavigateBack -> navigateBack()
        }
    }

    private fun loadForecast() {
        _state.update { it.copy(isLoading = true, error = null) }

        viewModelScope.launch {
            runCatching {
                val city = cityRepository.getSelectedCity()
                    ?: throw IllegalArgumentException("No city selected")

                val weather = weatherRepository.getCurrentWeather(city.latitude, city.longitude)
                val forecast = forecastRepository.getForecast(city.latitude, city.longitude, 40)
                val isFavorite = cityRepository.isCityFavorite(city)

                _state.update {
                    it.copy(
                        isLoading = false,
                        city = city,
                        currentWeather = weather,
                        forecast = forecast,
                        isFavorite = isFavorite,
                        error = null
                    )
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Error loading forecast"
                    )
                }
                _effects.emit(ForecastEffect.ShowMessage(e.message ?: "Error loading forecast"))
            }
        }
    }

    private fun toggleFavorite() {
        val currentState = _state.value
        val city = currentState.city ?: return

        viewModelScope.launch {
            if (currentState.isFavorite) {
                cityRepository.removeFavoriteCity(city)
                _state.update { it.copy(isFavorite = false) }
                _effects.emit(ForecastEffect.ShowMessage("Removed from favorites"))
            } else {
                cityRepository.addFavoriteCity(city)
                _state.update { it.copy(isFavorite = true) }
                _effects.emit(ForecastEffect.ShowMessage("Added to favorites"))
            }
        }
    }

    private fun shareWeather() {
        val currentState = _state.value
        val city = currentState.city
        val weather = currentState.currentWeather

        if (city != null && weather != null) {
            viewModelScope.launch {
                _effects.emit(ForecastEffect.ShareWeatherCopied)
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _effects.emit(ForecastEffect.NavigateBack)
        }
    }
}

class ForecastViewModelFactory(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ForecastViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ForecastViewModel(cityRepository, weatherRepository, forecastRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}