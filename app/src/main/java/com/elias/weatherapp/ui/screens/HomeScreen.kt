package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChangeCircle
import androidx.compose.material.icons.filled.Delete
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
    viewModel: WeatherAppViewModel = hiltViewModel(),
    onLocationChange: () -> Unit = {}
) {
    val weatherData by viewModel.weather.collectAsStateWithLifecycle()
    var isManualRefreshing by remember { mutableStateOf(false) }
    val displaySettings by viewModel.displaySettings.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadCurrentWeather(isBackgroundUpdate = false)

        while (true) {
            delay(15 * 60 * 1000)
            viewModel.loadCurrentWeather(isBackgroundUpdate = true)
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
                        text = stringResource(viewModel.errorMessageResId!!),
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
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = viewModel.cityName ?: stringResource(R.string.text_unknown_location),
                                style = MaterialTheme.typography.headlineMedium,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            val displaySettings by viewModel.displaySettings.collectAsStateWithLifecycle()

                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                WeatherItem(
                                    label = stringResource(R.string.text_current_temperature),
                                    value = "${data.temperature}°C",
                                    labelStyle = MaterialTheme.typography.titleMedium,
                                    valueStyle = MaterialTheme.typography.displayLarge
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                if (displaySettings.showApparentTemp) {
                                    WeatherItem(stringResource(R.string.text_apparent_temperature), "${data.apparentTemperature}°C")
                                }
                                if (displaySettings.showWind) {
                                    WeatherItem(
                                        stringResource(R.string.text_wind),
                                        "${data.windSpeed} km/h"
                                    )
                                }
                                if (displaySettings.showWindGusts && data.windGusts > 0.0) {
                                    WeatherItem(
                                        stringResource(R.string.text_wind_gusts),
                                        "${data.windGusts} km/h"
                                    )
                                }
                                if (displaySettings.showWindDirection) {
                                    WeatherItem(
                                        label = stringResource(R.string.text_wind_direction),
                                        value = stringResource(viewModel.getWindDirectionResId(data.windDirection))
                                    )
                                }
                                if (displaySettings.showHumidity) {
                                    WeatherItem(
                                        stringResource(R.string.text_humidity),
                                        "${data.humidity}%"
                                    )
                                }
                                if (displaySettings.showCloudCover) {
                                    WeatherItem(
                                        stringResource(R.string.text_cloud_cover),
                                        "${data.cloudCover}%"
                                    )
                                }
                                if (displaySettings.showPrecipitation && data.precipitation > 0.0) {
                                    WeatherItem(
                                        stringResource(R.string.text_precipitation),
                                        "${data.precipitation} mm"
                                    )
                                }
                                if (displaySettings.showSnowfall && data.snowfall > 0.0) {
                                    WeatherItem(
                                        stringResource(R.string.text_snowfall),
                                        "${data.snowfall} cm"
                                    )
                                }
                                if (displaySettings.showPressureMsl) {
                                    WeatherItem(
                                        label = stringResource(R.string.text_pressure_msl),
                                        value = "${data.pressureMsl} hPa"
                                    )
                                }
                                if (displaySettings.showSurfacePressure) {
                                    WeatherItem(
                                        label = stringResource(R.string.text_surface_pressure),
                                        value = "${data.surfacePressure} hPa"
                                    )
                                }
                            }
                        }
                    }
                    Button(
                        onClick = { viewModel.changeLocation(); onLocationChange() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.ChangeCircle, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.button_change_location))
                    }
                } else -> {
                    Text(
                        text = stringResource(R.string.error_no_weather_data),
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
