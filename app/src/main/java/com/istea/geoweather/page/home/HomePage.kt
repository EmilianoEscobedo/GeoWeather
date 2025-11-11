package com.istea.geoweather.page.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun HomePage(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeView(
        state = state,
        onIntent = { intent ->
            when (intent) {
                HomeIntent.NavigateToCity -> navController.navigate("city")
                HomeIntent.OpenAboutDialog -> viewModel.onIntent(intent)
                HomeIntent.CloseAboutDialog -> viewModel.onIntent(intent)
                HomeIntent.FinishLoading -> viewModel.onIntent(intent)
            }
        }
    )
}
