package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@Composable
fun HomeScreen(
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val weatherData by viewModel.weather.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.loadWeather()
    }

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                viewModel.isLoading -> {
                    CircularProgressIndicator()
                }

                viewModel.errorMessage != null -> {
                    Text(
                        text = viewModel.errorMessage!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Button(onClick = { viewModel.loadWeather() }) {
                        Text("Retry")
                    }
                }

                weatherData != null -> {
                    Text(
                        text = viewModel.cityName ?: "Unknown Location",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Current Temperature",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "${weatherData?.temperature}°C",
                        style = MaterialTheme.typography.displayLarge
                    )
                    Text(
                        text = "Wind",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${weatherData?.windSpeed} km/h",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(
                        text = "Humidity",
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${weatherData?.humidity}%",
                        style = MaterialTheme.typography.titleLarge
                    )

                }

                else -> {
                    Text("No weather data available.")
                }
            }
        }
    }
}
