package com.elias.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elias.weatherapp.R
import com.elias.weatherapp.data.model.domain.HourlyWeatherData
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DayWeatherScreen(
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val hourlyDataList by viewModel.hourlyWeather.collectAsStateWithLifecycle()
    var expandedIndex by remember { mutableStateOf<Int?>(null) }

    var isManualRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadHourlyWeather()
    }

    LaunchedEffect(viewModel.isHourlyLoading) {
        if (!viewModel.isHourlyLoading) {
            isManualRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isManualRefreshing,
        onRefresh = {
            isManualRefreshing = true
            viewModel.loadHourlyWeather(force = true)
        },
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.label_day),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                viewModel.isHourlyLoading && !isManualRefreshing -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                viewModel.hourlyErrorOccurred -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.error_failed_load_weather),
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadHourlyWeather() }) {
                                Text(stringResource(R.string.button_retry))
                            }
                        }
                    }
                }

                hourlyDataList.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.error_no_weather_data),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        itemsIndexed(hourlyDataList) { index, hourData ->
                            val isExpanded = index == expandedIndex

                            val displayTime = if (index == 0) {
                                stringResource(R.string.text_now)
                            } else {
                                hourData.time
                            }

                            ExpandableHourlyCard(
                                data = hourData,
                                displayTime = displayTime,
                                isExpanded = isExpanded,
                                viewModel = viewModel,
                                onClick = {
                                    expandedIndex = if (isExpanded) null else index
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExpandableHourlyCard(
    data: HourlyWeatherData,
    displayTime: String,
    isExpanded: Boolean,
    viewModel: WeatherAppViewModel,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isExpanded)
                MaterialTheme.colorScheme.primaryContainer
            else
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = displayTime,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = "${data.temperature}°C",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                val weatherEmoji = when {
                    data.precipitationProbability >= 60 -> "\uD83C\uDF27\uFE0F"
                    data.precipitationProbability >= 40 -> "\uD83C\uDF26\uFE0F"
                    else -> "\u2600\uFE0F"
                }

                Text(
                    text = "$weatherEmoji ${data.precipitationProbability}%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End,
                    color = MaterialTheme.colorScheme.outline
                )

            }

            AnimatedVisibility(visible = isExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 12.dp))

                    WeatherItem(stringResource(R.string.text_apparent_temperature), "${data.apparentTemperature}°C")
                    WeatherItem(stringResource(R.string.text_wind), "${data.windSpeed} km/h")
                    WeatherItem(stringResource(R.string.text_wind_direction), stringResource(id = viewModel.getWindDirectionResId(data.windDirection)))
                    WeatherItem(stringResource(R.string.text_wind_gusts), "${data.windGusts} km/h")
                    WeatherItem(stringResource(R.string.text_cloud_cover), "${data.cloudCover}%")
                    WeatherItem(stringResource(R.string.text_humidity), "${data.humidity}%")

                    if (data.precipitation > 0.0) {
                        WeatherItem(stringResource(R.string.text_precipitation), "${data.precipitation} mm")
                    }
                    if (data.snowfall > 0.0) {
                        WeatherItem(stringResource(R.string.text_snowfall), "${data.snowfall} cm")
                        WeatherItem(stringResource(R.string.text_snow_depth), "${data.snowDepth} m")
                    }

                    WeatherItem(stringResource(R.string.text_visibility), "${data.visibility} km")
                    WeatherItem(stringResource(R.string.text_pressure_msl), "${data.pressureMsl} hPa")
                    WeatherItem(stringResource(R.string.text_surface_pressure), "${data.surfacePressure} hPa")
                    WeatherItem(stringResource(R.string.text_soil_temperature), "${data.soilTemperature}°C")
                    WeatherItem(stringResource(R.string.text_soil_moisture), "${data.soilMoisture} m³/m³")
                }
            }
        }
    }
}
