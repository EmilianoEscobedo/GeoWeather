package com.istea.geoweather.presentation.home

import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.After
import org.junit.Test
import org.junit.Assert.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Duration.Companion.milliseconds
import com.istea.geoweather.mocks.router.RouterMock
import com.istea.geoweather.router.Route
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

class HomeViewModelTest {

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    val router = RouterMock()
    val factory = HomeViewModelFactory(router)
    val viewModel = factory.create(HomeViewModel::class.java)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        router.reset()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun finishLoading_sets_loading_false() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(HomeIntent.FinishLoading)
            delay(1.milliseconds)

            assertFalse(viewModel.state.value.isLoading)
        }
    }

    @Test
    fun openAboutDialog_shows_dialog() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(HomeIntent.OpenAboutDialog)
            delay(1.milliseconds)

            assertTrue(viewModel.state.value.showAboutDialog)
        }
    }

    @Test
    fun closeAboutDialog_hides_dialog() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(HomeIntent.OpenAboutDialog)
            delay(1.milliseconds)
            assertTrue(viewModel.state.value.showAboutDialog)

            viewModel.onIntent(HomeIntent.CloseAboutDialog)
            delay(1.milliseconds)

            assertFalse(viewModel.state.value.showAboutDialog)
        }
    }

    @Test
    fun navigateToCity_navigates_to_city_screen() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(HomeIntent.NavigateToCity)
            delay(1.milliseconds)

            assertEquals(Route.City, router.lastNavigatedRoute)
            assertEquals(1, router.navigationCallCount)
        }
    }
}