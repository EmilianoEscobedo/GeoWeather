package com.istea.geoweather.page.weather

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.ForecastRepository
import com.istea.geoweather.data.repository.WeatherRepository
import com.istea.geoweather.entity.City
import com.istea.geoweather.entity.Forecast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val FORECAST_LIMIT = 40

class WeatherViewModel (
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository,
    private val savedStateHandle: SavedStateHandle
): ViewModel(){
    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state.asStateFlow()
    private val selectedCity: String = savedStateHandle["cityName"] ?: ""
    init {
        if (selectedCity.isNotEmpty()) {
            loadWeather(selectedCity)
        }
    }
    fun onIntent(intent: WeatherIntent) {
        when (intent) {
            WeatherIntent.NavigateBack -> { /* Manejado por navController */ }
            WeatherIntent.retry -> retry()
            WeatherIntent.addCityToFavorite -> addCityToFavorite()
            WeatherIntent.removeCityFromFavorite -> removeCityFromFavorite()
        }
    }

    fun loadWeather(cityName: String){
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null)}
            val city = runCatching {
                cityRepository.getCityInfoByName(cityName, 1).firstOrNull()
            }.getOrNull()
            if(city == null){
                _state.update { it.copy(isLoading = false, error = "No se encontró la ciudad '$cityName'") }
                return@launch
            }
            fetchWeatherAndForecast(city)
        }
    }
    private suspend fun fetchWeatherAndForecast(city: City) {
        val currentWeatherResult = runCatching {
            weatherRepository.getCurrentWeather(city.latitude, city.longitude)
        }

        val forecastResult = runCatching {
            forecastRepository.getForecast(city.latitude, city.longitude, FORECAST_LIMIT)
        }

        val currentWeather = currentWeatherResult.getOrNull()
        val forecast = forecastResult.getOrNull()
        val error = currentWeatherResult.exceptionOrNull() ?: forecastResult.exceptionOrNull()

        _state.update {
            it.copy(
                isLoading = false,
                cityName = city.name,
                country = city.country,
                currentWeather = currentWeather,
                forecast = forecast,
                error = error?.message
            )
        }
    }
    private fun retry() {
        val cityName = _state.value.cityName
        if (cityName.isNotEmpty()) loadWeather(cityName)
    }
    private fun addCityToFavorite() {
        viewModelScope.launch {
            val cityName = _state.value.cityName
            if (cityName.isEmpty()) return@launch

            val city = cityRepository.getCityInfoByName(cityName, 1).firstOrNull()
            if (city != null) {
                cityRepository.addFavoriteCity(city)
                _state.update { it.copy(isFavorite = true) }
            }
        }
    }

    private fun removeCityFromFavorite() {
        viewModelScope.launch {
            val cityName = _state.value.cityName
            if (cityName.isEmpty()) return@launch

            cityRepository.removeFavoriteCity(cityName)
            _state.update { it.copy(isFavorite = false) }
        }
    }
}