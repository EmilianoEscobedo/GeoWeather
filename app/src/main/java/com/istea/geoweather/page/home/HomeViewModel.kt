package com.istea.geoweather.page.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel : ViewModel() {
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
            else -> {}
        }
    }
}