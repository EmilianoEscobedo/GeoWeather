package com.istea.geoweather.page.city

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.istea.geoweather.data.repository.CityRepository
import com.istea.geoweather.data.repository.WeatherRepository

class CityViewModelFactory(
    owner: SavedStateRegistryOwner,
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        if (modelClass.isAssignableFrom(CityViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CityViewModel(cityRepository, weatherRepository, handle) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
