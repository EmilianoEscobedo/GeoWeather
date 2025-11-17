package com.istea.geoweather.presentation.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.istea.geoweather.router.Router
import com.istea.geoweather.router.Route

@Composable
fun HomePage(
    router: Router,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeView(
        state = state,
        onIntent = { intent ->
            when (intent) {
                HomeIntent.NavigateToCity -> router.navigate(Route.City)
                HomeIntent.OpenAboutDialog -> viewModel.onIntent(intent)
                HomeIntent.CloseAboutDialog -> viewModel.onIntent(intent)
                HomeIntent.FinishLoading -> viewModel.onIntent(intent)
            }
        }
    )
}