package com.istea.geoweather.page.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.ForecastRepository
import com.istea.geoweather.data.repository.WeatherRepository
import com.istea.geoweather.entity.City
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SUGGESTION_LIMIT = 10
private const val FORECAST_LIMIT = 40
private const val SEARCH_DEBOUNCE_MS = 350L

class CityViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(CityState())
    val state: StateFlow<CityState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onIntent(intent: CityIntent) {
        when (intent) {
            is CityIntent.searchCity   -> onSearchTextChanged(intent.text)
            is CityIntent.showWeather  -> showWeatherByName(intent.text)
            is CityIntent.selectCity   -> loadForCity(intent.city, fromSelection = true)
            CityIntent.refresh         -> refresh()
            CityIntent.getDevicePosition -> {}
        }
    }

    // --- SEARCH WITH debounce ---
    private fun onSearchTextChanged(value: String) {
        _state.update { it.copy(text = value) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val q = value.trim()
            if (q.length < 2) {
                _state.update { it.copy(filterList = emptyList()) }
                return@launch
            }
            delay(SEARCH_DEBOUNCE_MS)
            runCatching { cityRepository.getCityInfoByName(q, SUGGESTION_LIMIT) }
                .onSuccess { cities -> _state.update { it.copy(filterList = cities, error = null) } }
                .onFailure { e -> _state.update { it.copy(filterList = emptyList(), error = e.message ?: "Error buscando ciudades") } }
        }
    }

    // --- LOAD FOR NAME  ---
    private fun showWeatherByName(cityName: String) {
        val q = cityName.trim()
        if (q.isEmpty()) return
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            val found = runCatching { cityRepository.getCityInfoByName(q, 1).firstOrNull() }
            found.onSuccess { city ->
                if (city != null) {
                    loadForCity(city, fromSelection = false)
                } else {
                    _state.update { it.copy(isLoading = false, error = "No se encontró la ciudad '$q'") }
                }
            }.onFailure { e ->
                _state.update { it.copy(isLoading = false, error = e.message ?: "Error buscando '$q'") }
            }
        }
    }

    // --- CORE LOAD (weather + forecast) ---
    private fun loadForCity(city: City, fromSelection: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    selectedCity = city,
                    filterList = if (fromSelection) emptyList() else it.filterList,
                    error = null
                )
            }

            val current = runCatching { weatherRepository.getCurrentWeather(city.latitude, city.longitude) }
            val forecast = runCatching { forecastRepository.getForecast(city.latitude, city.longitude, FORECAST_LIMIT) }

            val cur = current.getOrNull()
            val forc = forecast.getOrNull()
            val err = current.exceptionOrNull() ?: forecast.exceptionOrNull()

            if (err == null && cur != null && forc != null) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        weather = cur,
                        forecast = forc,
                       
                        city = city.name,
                        country = city.country,
                        temperature = "${cur.temperature.toInt()}°",
                        error = null
                    )
                }
            } else {
                _state.update { it.copy(isLoading = false, error = err?.message ?: "No se pudo cargar el clima") }
            }
        }
    }

    // --- REFRESH ---
    private fun refresh() {
        val city = state.value.selectedCity ?: return
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }

            val current = runCatching { weatherRepository.getCurrentWeather(city.latitude, city.longitude) }
            val forecast = runCatching { forecastRepository.getForecast(city.latitude, city.longitude, FORECAST_LIMIT) }

            val cur = current.getOrNull()
            val forc = forecast.getOrNull()
            val err = current.exceptionOrNull() ?: forecast.exceptionOrNull()

            _state.update {
                it.copy(
                    isRefreshing = false,
                    weather = cur ?: it.weather,
                    forecast = forc ?: it.forecast,
                    temperature = cur?.let { w -> "${w.temperature.toInt()}°" } ?: it.temperature,
                    error = err?.message
                )
            }
        }
    }
}
