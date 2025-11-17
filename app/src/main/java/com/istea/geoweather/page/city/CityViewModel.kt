package com.istea.geoweather.page.city

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.WeatherRepository
import com.istea.geoweather.entity.City
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
private const val SEARCH_DEBOUNCE_MS = 2000L

sealed class CityEffect {
    data class ShowMessage(val text: String) : CityEffect()
    object NavigateToWeather : CityEffect()
}

class CityViewModel(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
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
    private var locationData: LocationData? = null

    data class LocationData(
        val latitude: Double,
        val longitude: Double,
        val allowed: Boolean
    )

    fun onIntent(intent: CityIntent) {
        when (intent) {
            is CityIntent.searchCity -> handleSearchCity(intent.text)
            is CityIntent.selectCity -> handleSelectCity(intent.city)
            is CityIntent.selectFavoriteCity -> handleSelectFavoriteCity(intent.city)
            CityIntent.navigateToExtendedForecast -> handleNavigateToExtendedForecast()
            CityIntent.getDevicePosition -> handleDevicePosition()
            CityIntent.FinishLoading -> _state.update { it.copy(isLoading = false) }
            CityIntent.RefreshFavorites -> refreshFavorites()
            is CityIntent.toggleFavorite -> handleToggleFavorite(intent.city)
        }
    }

    private fun refreshFavorites() {
        _state.update {
            it.copy(favoriteCityList = cityRepository.getFavoriteCities())
        }
    }

    private fun handleToggleFavorite(city: City) {
        val isFavorite = cityRepository.isCityFavorite(city)
        if (isFavorite) {
            cityRepository.removeFavoriteCity(city)
        } else {
            cityRepository.addFavoriteCity(city)
        }
        _state.update {
            it.copy(favoriteCityList = cityRepository.getFavoriteCities())
        }
    }

    private fun handleSelectFavoriteCity(city: City) {
        viewModelScope.launch {
            cityRepository.setSelectedCity(city)
            _effects.emit(CityEffect.NavigateToWeather)
        }
    }

    private fun handleSearchCity(text: String) {
        _state.update {
            it.copy(
                text = text,
                showNoResults = false,
                selectedCity = null
            )
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            val query = text.trim()
            if (query.length < 2) {
                _state.update {
                    it.copy(
                        filterList = emptyList(),
                        isSearching = false,
                        showNoResults = false
                    )
                }
                return@launch
            }

            _state.update { it.copy(isSearching = true) }
            delay(SEARCH_DEBOUNCE_MS)

            runCatching {
                cityRepository.getCityInfoByName(query, SUGGESTION_LIMIT)
            }.onSuccess { cities ->
                _state.update { current ->
                    current.copy(
                        filterList = cities,
                        favoriteCityList = cityRepository.getFavoriteCities(),
                        isSearching = false,
                        showNoResults = cities.isEmpty()
                    )
                }
            }.onFailure { e ->
                _effects.emit(
                    CityEffect.ShowMessage(
                        e.message ?: "Error searching cities"
                    )
                )
                _state.update {
                    it.copy(
                        filterList = emptyList(),
                        isSearching = false,
                        showNoResults = true
                    )
                }
            }
        }
    }

    private fun handleSelectCity(city: City) {
        viewModelScope.launch {
            cityRepository.setSelectedCity(city)
            _state.update {
                it.copy(
                    selectedCity = city,
                    filterList = emptyList(),
                    text = ""
                )
            }
            loadCityWeather(city)
        }
    }

    private fun handleNavigateToExtendedForecast() {
        viewModelScope.launch {
            _effects.emit(CityEffect.NavigateToWeather)
        }
    }

    fun saveLocationInHandle(latitude: Double, longitude: Double, allowed: Boolean) {
        locationData = LocationData(latitude, longitude, allowed)
    }

    private fun handleDevicePosition() {
        val location = locationData

        _state.update { it.copy(geolocationAllowed = location?.allowed ?: true) }

        if (location?.allowed != true) {
            viewModelScope.launch {
                _effects.emit(
                    CityEffect.ShowMessage("Location permission denied")
                )
            }
            return
        }

        if (location.latitude == 0.0 && location.longitude == 0.0) {
            viewModelScope.launch {
                _effects.emit(
                    CityEffect.ShowMessage("Location not available")
                )
            }
            return
        }

        _state.update { it.copy(isLoadingGeolocation = true) }

        viewModelScope.launch {
            runCatching {
                cityRepository.getCityInfoByCoordinates(location.latitude, location.longitude, 1).firstOrNull()
            }.onSuccess { city ->
                if (city != null) {
                    cityRepository.setSelectedCity(city)
                    loadCityWeather(city)
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
            }.also {
                _state.update { it.copy(isLoadingGeolocation = false) }
            }
        }
    }

    private suspend fun loadCityWeather(city: City) {
        runCatching {
            weatherRepository.getCurrentWeather(city.latitude, city.longitude)
        }.onSuccess { weather ->
            _state.update {
                it.copy(
                    city = city.name,
                    country = city.country,
                    flag = city.flag,
                    latitude = city.latitude,
                    longitude = city.longitude,
                    currentCityWeather = weather
                )
            }
        }.onFailure { e ->
            _effects.emit(
                CityEffect.ShowMessage(
                    e.message ?: "Could not load weather"
                )
            )
        }
    }
}

class CityViewModelFactory(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(cityRepository, weatherRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}