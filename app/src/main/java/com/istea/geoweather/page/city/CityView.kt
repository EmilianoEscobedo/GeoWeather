package com.istea.geoweather.page.city

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.istea.geoweather.entity.City
import kotlinx.coroutines.delay
import androidx.compose.ui.unit.sp
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search

@Composable
fun CityView(
    state: CityState,
    onIntent: (CityIntent) -> Unit,
    ciudades: List<City>,
    onBuscarCiudad: (String) -> Unit,
    onSeleccionarCiudad: (City) -> Unit,
    ciudadesFavoritas: List<City> = emptyList(),
    ciudadActual: City? = null
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
                    text = "CLIMA",
                    style = MaterialTheme.typography.headlineLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                CitySearchBar(onBuscarCiudad = onBuscarCiudad)

                Spacer(modifier = Modifier.height(16.dp))

                when {
                    ciudadActual != null && state.text.isEmpty() -> {
                        CityCardSimple(
                            city = ciudadActual,
                            onClick = { onSeleccionarCiudad(ciudadActual) }
                        )
                    }

                    state.text.isNotEmpty() && ciudades.isNotEmpty() -> {
                        LazyColumn(
                            modifier = Modifier.weight(1f)
                        ) {
                            items(ciudades) { ciudad ->
                                CityCardSimple(city = ciudad) { onSeleccionarCiudad(ciudad) }
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
                                    text = "🌤️ Buscá una ciudad o activá tu ubicación",
                                    style = MaterialTheme.typography.titleMedium,
                                    textAlign = TextAlign.Center
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "para ver el clima actual.",
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
                    text = "Ciudades Favoritas",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                if (ciudadesFavoritas.isEmpty()) {
                    Text("NO HAY CIUDADES AÑADIDAS A FAVORITOS")
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ciudadesFavoritas.forEach { ciudad ->
                            BanderaFavorita(ciudad)
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
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = getFlagEmoji(city.country),
                fontSize = 48.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = city.name,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}


@Composable
fun BanderaFavorita(city: City) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = getFlagEmoji(city.country),
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

fun getFlagEmoji(countryCode: String): String {
    return countryCode.uppercase()
        .map { char -> Character.codePointAt(charArrayOf(char), 0) - 65 + 0x1F1E6 }
        .joinToString("") { code -> String(Character.toChars(code)) }
}

@Composable
fun CitySearchBar(
    onBuscarCiudad: (String) -> Unit
) {
    var query by remember { mutableStateOf("") }

    OutlinedTextField(
        value = query,
        onValueChange = {
            query = it
            onBuscarCiudad(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        placeholder = { Text("Ingrese la Ciudad") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Buscar",
                tint = MaterialTheme.colorScheme.primary
            )
        },

        shape = RoundedCornerShape(12.dp),
        singleLine = true
    )
}
