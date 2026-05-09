package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elias.weatherapp.R
import com.elias.weatherapp.viewmodel.WeatherAppViewModel
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val weatherData by viewModel.weather.collectAsStateWithLifecycle()

    var isManualRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        while (true) {
            viewModel.loadWeather()
            delay(15 * 60 * 1000)
        }
    }
    LaunchedEffect(viewModel.isLoading) {
        if (!viewModel.isLoading) {
            isManualRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isManualRefreshing,
        onRefresh = {
            isManualRefreshing = true
            viewModel.loadWeather()
                    },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            when {
                viewModel.isLoading && weatherData == null -> {
                    CircularProgressIndicator()
                }

                viewModel.errorMessageResId != null && weatherData == null -> {
                    Text(
                        text = stringResource(id = viewModel.errorMessageResId!!),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = {
                        viewModel.retry()
                    }) {
                        Text("Retry")
                    }
                }

                weatherData != null -> {
                    Text(
                        text = viewModel.cityName ?: stringResource(id = R.string.text_unknown_location),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.text_current_temperature),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        text = "${weatherData?.temperature}°C",
                        style = MaterialTheme.typography.displayLarge
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = stringResource(id = R.string.text_wind),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${weatherData?.windSpeed} km/h",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.text_humidity),
                        style = MaterialTheme.typography.labelMedium
                    )
                    Text(
                        text = "${weatherData?.humidity}%",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                else -> {
                    Text(text = stringResource(id = R.string.error_no_weather_data))
                }
            }
        }
    }
}
