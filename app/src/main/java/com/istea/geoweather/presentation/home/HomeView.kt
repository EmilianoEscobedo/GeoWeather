package com.istea.geoweather.presentation.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.istea.geoweather.R
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    state: HomeState,
    onIntent: (HomeIntent) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    LaunchedEffect(Unit) {
        delay(2000)
        onIntent(HomeIntent.FinishLoading)
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
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Spacer(modifier = Modifier.weight(0.5f))

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null,
                    modifier = Modifier.size(280.dp)
                )

                Button(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    onClick = { onIntent(HomeIntent.NavigateToCity) }
                ) {
                    Text(
                        text = "Discover Today's Forecast",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { onIntent(HomeIntent.OpenAboutDialog) }
                ) {
                    Text(
                        text = "About GeoWeather",
                        style = MaterialTheme.typography.labelLarge
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        if (state.showAboutDialog) {
            ModalBottomSheet(
                onDismissRequest = { onIntent(HomeIntent.CloseAboutDialog) },
                sheetState = bottomSheetState,
                modifier = Modifier.fillMaxWidth()
            ) {
                AboutDialogContent()
            }
        }
    }
}

@Composable
private fun AboutDialogContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp)
    ) {
        Text(
            text = "About GeoWeather",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Text(
            text = "GeoWeather is an Android weather app with geolocation capabilities, built using MVI architecture. " +
                    "It was developed as a midterm exam project for the ISTEA Mobile Applications course.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        )

        Text(
            text = "Meet the Team",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        TeamMember(
            name = "Nicolas Gonzalez",
            role = "UX/UI design and development",
            image = R.drawable.nicolas_gonzalez_profile
        )

        TeamMember(
            name = "Santiago Andini",
            role = "UI development and testing",
            image = R.drawable.santiago_andini_profile
        )

        TeamMember(
            name = "Nicolas Ibarra",
            role = "UI development and testing",
            image = R.drawable.nicolas_ibarra_profile
        )

        TeamMember(
            name = "Juan Picabea",
            role = "UI development and testing",
            image = R.drawable.juan_picabea_profile
        )

        TeamMember(
            name = "Emiliano Escobedo",
            role = "API connectivity and UI development",
            image = R.drawable.emiliano_escobedo_profile
        )


        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun TeamMember(
    name: String,
    role: String,
    image: Int
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = image),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = role,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}