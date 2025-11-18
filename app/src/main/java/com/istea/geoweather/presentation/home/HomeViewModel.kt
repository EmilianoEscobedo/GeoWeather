package com.istea.geoweather.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.istea.geoweather.router.Router
import com.istea.geoweather.router.Route
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel(
    private val router: Router
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState(isLoading = true))
    val state: StateFlow<HomeState> = _state.asStateFlow()

    fun onIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.FinishLoading -> {
                _state.value = _state.value.copy(isLoading = false)
            }
            is HomeIntent.OpenAboutDialog -> {
                _state.value = _state.value.copy(showAboutDialog = true)
            }
            is HomeIntent.CloseAboutDialog -> {
                _state.value = _state.value.copy(showAboutDialog = false)
            }
            HomeIntent.NavigateToCity -> navigateToCity()
        }
    }

    private fun navigateToCity() {
        viewModelScope.launch {
            router.navigate(Route.City)
        }
    }
}

class HomeViewModelFactory(
    private val router: Router
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(router) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}