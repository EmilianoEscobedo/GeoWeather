package com.istea.geoweather.presentation.forecast

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
import com.istea.geoweather.mocks.repository.CityRepositoryMockForecast
import com.istea.geoweather.mocks.repository.CityRepositoryMockForecastNoCity
import com.istea.geoweather.mocks.repository.ForecastRepositoryMock
import com.istea.geoweather.mocks.repository.WeatherRepositoryMock
import com.istea.geoweather.mocks.router.RouterMock
import com.istea.geoweather.router.Route
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

class ForecastViewModelTest {

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    val cityRepository = CityRepositoryMockForecast()
    val weatherRepository = WeatherRepositoryMock()
    val forecastRepository = ForecastRepositoryMock()
    val router = RouterMock()

    val factory = ForecastViewModelFactory(cityRepository, weatherRepository, forecastRepository, router)
    val viewModel = factory.create(ForecastViewModel::class.java)

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
    fun loadForecast_success() = runTest(timeout = 3.seconds) {
        val expectedState = ForecastState(
            isLoading = false,
            city = cityRepository.testCity,
            currentWeather = weatherRepository.testWeather,
            forecast = forecastRepository.testForecast,
            isFavorite = false,
            error = null
        )

        launch(Dispatchers.Main) {
            viewModel.onIntent(ForecastIntent.LoadForecast)
            delay(1.milliseconds)
            assertEquals(expectedState, viewModel.state.value)
        }
    }

    @Test
    fun loadForecast_no_city() = runTest(timeout = 3.seconds) {
        val repositoryNoCity = CityRepositoryMockForecastNoCity()
        val factory = ForecastViewModelFactory(repositoryNoCity, weatherRepository, forecastRepository, router)
        val vm = factory.create(ForecastViewModel::class.java)

        val expectedState = ForecastState(
            isLoading = false,
            city = null,
            currentWeather = null,
            forecast = null,
            isFavorite = false,
            error = "No city selected"
        )

        launch(Dispatchers.Main) {
            vm.onIntent(ForecastIntent.LoadForecast)
            delay(1.milliseconds)
            assertEquals(expectedState, vm.state.value)
        }
    }

    @Test
    fun toggleFavorite_add() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(ForecastIntent.LoadForecast)
            delay(1.milliseconds)

            assertFalse(viewModel.state.value.isFavorite)

            viewModel.onIntent(ForecastIntent.ToggleFavorite)
            delay(1.milliseconds)

            assertTrue(viewModel.state.value.isFavorite)
        }
    }

    @Test
    fun toggleFavorite_remove() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(ForecastIntent.LoadForecast)
            delay(1.milliseconds)

            viewModel.onIntent(ForecastIntent.ToggleFavorite)
            delay(1.milliseconds)
            assertTrue(viewModel.state.value.isFavorite)

            viewModel.onIntent(ForecastIntent.ToggleFavorite)
            delay(1.milliseconds)

            assertFalse(viewModel.state.value.isFavorite)
        }
    }

    @Test
    fun navigateBack_navigates_to_city() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(ForecastIntent.NavigateBack)
            delay(10.milliseconds)

            assertEquals(Route.City, router.lastNavigatedRoute)
            assertEquals(1, router.navigationCallCount)
        }
    }
}