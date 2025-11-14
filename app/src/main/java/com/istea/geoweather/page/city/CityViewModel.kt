package com.istea.geoweather.page.city

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.WeatherRepository
import com.istea.geoweather.entity.City
import com.istea.geoweather.entity.Weather
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val SUGGESTION_LIMIT = 10
private const val SEARCH_DEBOUNCE_MS = 350L

sealed class CityEffect {
    data class ShowMessage(val text: String) : CityEffect()
    data class NavigateToForecast(val weather: Weather) : CityEffect()
}

class CityViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

       private val _state = MutableStateFlow(
        CityState(
            text = "",
            filterList = emptyList(),
            favoriteCityList = cityRepository.getFavoriteCities(),
            geolocationAllowed = true,
            city = "",
            country = "",
            latitude = 0.0,
            longitude = 0.0
        )
    )
    val state: StateFlow<CityState> = _state.asStateFlow()


    private val _effects = MutableSharedFlow<CityEffect>()
    val effects: SharedFlow<CityEffect> = _effects.asSharedFlow()

    private var searchJob: Job? = null

    fun onIntent(intent: CityIntent) {
        when (intent) {
            is CityIntent.searchCity      -> handleSearchCity(intent.text)
            is CityIntent.showWeather     -> handleShowWeather(intent.text)
            CityIntent.getDevicePosition -> handleDevicePosition()
            CityIntent.FinishLoading      -> _state.update { it.copy(isLoading = false) }
        }
    }

    private fun handleSearchCity(text: String) {
        _state.update { it.copy(text = text) }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val query = text.trim()
            if (query.length < 2) {
                _state.update { it.copy(filterList = emptyList()) }
                return@launch
            }

            delay(SEARCH_DEBOUNCE_MS)

            runCatching {
                cityRepository.getCityInfoByName(query, SUGGESTION_LIMIT)
            }.onSuccess { cities ->
                _state.update { current ->
                    current.copy(
                        filterList = cities,
                        favoriteCityList = cityRepository.getFavoriteCities()
                    )
                }
            }.onFailure { e ->
                _effects.emit(
                    CityEffect.ShowMessage(
                        e.message ?: "Error searching cities"
                    )
                )
                _state.update { it.copy(filterList = emptyList()) }
            }
        }
    }

    private fun handleShowWeather(text: String) {
        val query = text.trim()
        if (query.isEmpty()) return

        viewModelScope.launch {
            runCatching {
                cityRepository.getCityInfoByName(query, 1).firstOrNull()
            }.onSuccess { city ->
                if (city != null) {
                    loadCityWeather(city, navigateToForecast = true)
                } else {
                    _effects.emit(
                        CityEffect.ShowMessage(
                            "City '$query' not found"
                        )
                    )
                }
            }.onFailure { e ->
                _effects.emit(
                    CityEffect.ShowMessage(
                        e.message ?: "Error searching '$query'"
                    )
                )
            }
        }
    }

    fun saveLocationInHandle(latitude: Double, longitude: Double, allowed: Boolean) {
        savedStateHandle["latitude"] = latitude
        savedStateHandle["longitude"] = longitude
        savedStateHandle["geolocationAllowed"] = allowed
    }

    private fun handleDevicePosition() {
        val allowed: Boolean = savedStateHandle.get<Boolean>("geolocationAllowed") ?: true
        val lat: Double? = savedStateHandle.get<Double>("latitude")
        val lon: Double? = savedStateHandle.get<Double>("longitude")

        _state.update { it.copy(geolocationAllowed = allowed) }

        if (!allowed) {
            viewModelScope.launch {
                _effects.emit(
                    CityEffect.ShowMessage("Location permission denied")
                )
            }
            return
        }

        if (lat == null || lon == null) {
            viewModelScope.launch {
                _effects.emit(
                    CityEffect.ShowMessage("Location not available")
                )
            }
            return
        }

        viewModelScope.launch {
            runCatching {
                cityRepository.getCityInfoByCoordinates(lat, lon, 1).firstOrNull()
            }.onSuccess { city ->
                if (city != null) {
                    loadCityWeather(city, navigateToForecast = false)
                } else {
                    _effects.emit(
                        CityEffect.ShowMessage("Could not resolve city from location")
                    )
                }
            }.onFailure { e ->
                _effects.emit(
                    CityEffect.ShowMessage(
                        e.message ?: "Error resolving location"
                    )
                )
            }
        }
    }

      private suspend fun loadCityWeather(
        city: City,
        navigateToForecast: Boolean
    ) {
        runCatching {
            weatherRepository.getCurrentWeather(city.latitude, city.longitude)
        }.onSuccess { weather ->
            _state.update {
                it.copy(
                    city = city.name,
                    country = city.country,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    currentCityWeather = weather
                )
            }
            if (navigateToForecast) {
                _effects.emit(CityEffect.NavigateToForecast(weather))
            }
        }.onFailure { e ->
            _effects.emit(
                CityEffect.ShowMessage(
                    e.message ?: "Could not load weather"
                )
            )
        }
    }


    fun addFavorite(city: City) {
        cityRepository.addFavoriteCity(city)
        _state.update {
            it.copy(favoriteCityList = cityRepository.getFavoriteCities())
        }
    }

    fun removeFavorite(cityName: String) {
        cityRepository.removeFavoriteCity(cityName)
        _state.update {
            it.copy(favoriteCityList = cityRepository.getFavoriteCities())
        }
    }
}
