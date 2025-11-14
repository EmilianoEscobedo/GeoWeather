package com.istea.geoweather.page.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.istea.geoweather.entity.City
import kotlinx.coroutines.delay
import coil.compose.AsyncImage

@Composable
fun CityView(
    state: CityState,
    onIntent: (CityIntent) -> Unit,
    cities: List<City>,
    favoriteCities: List<City> = emptyList(),
    currentCity: City? = null
) {
    LaunchedEffect(Unit) {
        delay(2000)
        onIntent(CityIntent.FinishLoading)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = MaterialTheme.colorScheme.primary
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Weather",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                CitySearchBar(onSearchCity = { query ->
                    onIntent(CityIntent.searchCity(query))
                })

                Spacer(modifier = Modifier.height(16.dp))

                val weather = state.currentCityWeather
                val temp = weather?.temperature?.toInt()?.toString()
                val iconUrl = weather?.iconUrl

                when {
                    currentCity != null && state.text.isEmpty() -> {
                        CityCardDetailed(
                            city = currentCity,
                            temperature = temp,
                            weatherIconUrl = iconUrl,
                            // El click en la tarjeta ahora llama a onIntent
                            onClick = { onIntent(CityIntent.showWeather(currentCity.name)) }
                        )
                    }

                    state.text.isNotEmpty() && cities.size == 1 -> {
                        val city = cities.first()
                        CityCardDetailed(
                            city = city,
                            temperature = temp,
                            weatherIconUrl = iconUrl,
                            // El click en la tarjeta ahora llama a onIntent
                            onClick = { onIntent(CityIntent.showWeather(city.name)) }
                        )
                    }

                    state.text.isNotEmpty() && cities.isNotEmpty() -> {
                        LazyColumn(modifier = Modifier.weight(1f)) {
                            items(cities) { city ->
                                CityCardSimple(
                                    city = city,
                                    // El click en el item de la lista ahora llama a onIntent
                                    onClick = { onIntent(CityIntent.showWeather(city.name)) }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }


                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "🌤️ Search for a city or enable your location",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "to see the current weather.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Favorite Cities",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (favoriteCities.isEmpty()) {
                    Text("NO FAVORITE CITIES ADDED")
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        favoriteCities.forEach { city ->
                            FavoriteFlag(city)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CityCardSimple(
    city: City,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                text = getFlagEmoji(city.country),
                fontSize = 42.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}



@Composable
fun CityCardDetailed(
    city: City,
    temperature: String?,
    weatherIconUrl: String?,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Your Weather",
                fontSize = 32.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = getFlagEmoji(city.country),
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (weatherIconUrl != null) {
                AsyncImage(
                    model = weatherIconUrl,
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(100.dp)
                )
            }
            if (temperature != null) {
                Text(
                    text = "$temperature°C",
                    style = MaterialTheme.typography.displayMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}


@Composable
fun FavoriteFlag(city: City) {
    Card(
        modifier = Modifier
            .size(90.dp)
            .padding(4.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = getFlagEmoji(city.country),
                fontSize = 40.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = city.name,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}


fun getFlagEmoji(countryCode: String): String {
    return countryCode.uppercase()
        .map { char -> Character.codePointAt(charArrayOf(char), 0) - 65 + 0x1F1E6 }
        .joinToString("") { code -> String(Character.toChars(code)) }
}

@Composable
fun CitySearchBar(
    onSearchCity: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onSearchCity(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = { Text("Enter a city") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
