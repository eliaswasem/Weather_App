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
import androidx.compose.ui.text.TextStyle
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
            viewModel.loadCurrentWeather()
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
            viewModel.loadCurrentWeather()
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            when {
                viewModel.isLoading && weatherData == null -> {
                    CircularProgressIndicator(modifier = Modifier.padding(top = 100.dp))
                }

                viewModel.errorMessageResId != null && weatherData == null -> {
                    Text(
                        text = stringResource(id = viewModel.errorMessageResId!!),
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { viewModel.retry() }) {
                        Text("Retry")
                    }
                }

                weatherData != null -> {
                    weatherData?.let { data ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = viewModel.cityName ?: stringResource(id = R.string.text_unknown_location),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            WeatherItem(stringResource(id = R.string.text_current_temperature), "${data.temperature}", MaterialTheme.typography.titleMedium, MaterialTheme.typography.displayLarge)
                            WeatherItem(stringResource(R.string.text_apparent_temperature), "${data.apparentTemperature}°C")
                            WeatherItem(stringResource(R.string.text_wind), "${data.windSpeed} km/h")
                            WeatherItem(stringResource(R.string.text_humidity), "${data.humidity}%")
                            WeatherItem(stringResource(R.string.text_cloud_cover), "${data.cloudCover}%")
                            WeatherItem(stringResource(R.string.text_wind_gusts), "${data.windGusts} km/h")
                            if (data.precipitation > 0.0) {
                                WeatherItem(
                                    stringResource(R.string.text_precipitation),
                                    "${data.precipitation} mm"
                                )
                            }
                            WeatherItem(stringResource(R.string.text_pressure_msl), "${data.pressureMsl} hPa")
                            WeatherItem(stringResource(R.string.text_wind_direction), stringResource(id = viewModel.getWindDirectionResId(data.windDirection)))
                            WeatherItem(stringResource(R.string.text_surface_pressure), "${data.surfacePressure} hPa")
                            if (data.snowfall > 0.0) {
                                WeatherItem(
                                    stringResource(R.string.text_snowfall),
                                    "${data.snowfall} cm"
                                )
                            }
                        }
                    }
                }

                else -> {
                    Text(
                        text = stringResource(id = R.string.error_no_weather_data),
                        modifier = Modifier.padding(top = 100.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun WeatherItem(label: String, value: String, labelStyle: TextStyle = MaterialTheme.typography.labelMedium, valueStyle: TextStyle = MaterialTheme.typography.titleLarge ) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = label, style = labelStyle)
        Text(text = value, style = valueStyle)
        Spacer(modifier = Modifier.height(8.dp))
    }
}
