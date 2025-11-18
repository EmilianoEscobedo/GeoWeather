package com.istea.geoweather.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.istea.geoweather.router.Router

@Composable
fun HomePage(
    router: Router
) {
    val factory = HomeViewModelFactory(router)
    val viewModel: HomeViewModel = viewModel(factory = factory)
    val state by viewModel.state.collectAsState()

    HomeView(
        state = state,
        onIntent = viewModel::onIntent
    )
}