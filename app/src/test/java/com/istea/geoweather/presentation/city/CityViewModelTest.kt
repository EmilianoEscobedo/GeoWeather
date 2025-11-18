package com.istea.geoweather.presentation.city

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
import com.istea.geoweather.mocks.repository.CityRepositoryMock
import com.istea.geoweather.mocks.repository.WeatherRepositoryMock
import com.istea.geoweather.mocks.router.RouterMock
import com.istea.geoweather.router.Route
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi

class CityViewModelTest {

    @OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    val cityRepository = CityRepositoryMock()
    val weatherRepository = WeatherRepositoryMock()
    val router = RouterMock()

    val factory = CityViewModelFactory(cityRepository, weatherRepository, router)
    val viewModel = factory.create(CityViewModel::class.java)

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
    fun searchCity_found() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.searchCity("cor"))
            delay(2100.milliseconds)

            assertTrue(viewModel.state.value.filterList.contains(cityRepository.cordoba))
        }
    }

    @Test
    fun searchCity_empty() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.searchCity("xyz"))
            delay(2100.milliseconds)

            assertTrue(viewModel.state.value.filterList.isEmpty())
            assertTrue(viewModel.state.value.showNoResults)
        }
    }

    @Test
    fun selectCity_updates_current() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.selectCity(cityRepository.cordoba))
            delay(100.milliseconds)

            assertEquals(cityRepository.cordoba.name, viewModel.state.value.city)
            assertEquals(cityRepository.cordoba.country, viewModel.state.value.country)
            assertNotNull(viewModel.state.value.currentCityWeather)
        }
    }

    @Test
    fun selectFavoriteCity_navigates_to_weather() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.selectFavoriteCity(cityRepository.cordoba))
            delay(10.milliseconds)

            assertEquals(Route.Forecast, router.lastNavigatedRoute)
            assertEquals(1, router.navigationCallCount)
        }
    }

    @Test
    fun navigateToExtendedForecast_navigates_to_weather() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.navigateToExtendedForecast)
            delay(10.milliseconds)

            assertEquals(Route.Forecast, router.lastNavigatedRoute)
            assertEquals(1, router.navigationCallCount)
        }
    }

    @Test
    fun toggleFavorite_add() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.toggleFavorite(cityRepository.cordoba))
            delay(100.milliseconds)

            assertTrue(cityRepository.isCityFavorite(cityRepository.cordoba))
            assertTrue(viewModel.state.value.favoriteCityList.contains(cityRepository.cordoba))
        }
    }

    @Test
    fun toggleFavorite_remove() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            cityRepository.addFavoriteCity(cityRepository.cordoba)

            viewModel.onIntent(CityIntent.toggleFavorite(cityRepository.cordoba))
            delay(100.milliseconds)

            assertFalse(cityRepository.isCityFavorite(cityRepository.cordoba))
            assertFalse(viewModel.state.value.favoriteCityList.contains(cityRepository.cordoba))
        }
    }

    @Test
    fun refreshFavorites_updates_list() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            cityRepository.addFavoriteCity(cityRepository.cordoba)

            viewModel.onIntent(CityIntent.RefreshFavorites)
            delay(100.milliseconds)

            assertTrue(viewModel.state.value.favoriteCityList.contains(cityRepository.cordoba))
        }
    }

    @Test
    fun finishLoading_sets_loading_false() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.onIntent(CityIntent.FinishLoading)
            delay(100.milliseconds)

            assertFalse(viewModel.state.value.isLoading)
        }
    }

    @Test
    fun getDevicePosition_with_location() = runTest(timeout = 3.seconds) {
        launch(Dispatchers.Main) {
            viewModel.saveLocationInHandle(43.2951, 5.3678, true)

            viewModel.onIntent(CityIntent.getDevicePosition)
            delay(100.milliseconds)

            assertTrue(viewModel.state.value.geolocationAllowed)
            assertFalse(viewModel.state.value.isLoadingGeolocation)
        }
    }
}