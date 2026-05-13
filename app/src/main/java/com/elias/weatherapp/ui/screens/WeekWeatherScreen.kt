package com.elias.weatherapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elias.weatherapp.R
import com.elias.weatherapp.data.model.domain.DailyWeatherData
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeekWeatherScreen(
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val dailyDataList by viewModel.dailyWeather.collectAsStateWithLifecycle()
    var expandedIndex by remember { mutableStateOf<Int?>(null) }
    var isManualRefreshing by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadDailyWeather()
    }

    LaunchedEffect(viewModel.isDailyLoading) {
        if (!viewModel.isDailyLoading) {
            isManualRefreshing = false
        }
    }

    PullToRefreshBox(
        isRefreshing = isManualRefreshing,
        onRefresh = {
            isManualRefreshing = true
            viewModel.loadDailyWeather(force = true)
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
                text = stringResource(R.string.label_week),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            when {
                viewModel.isDailyLoading && !isManualRefreshing -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                viewModel.dailyErrorOccurred -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.error_failed_load_weather),
                                color = MaterialTheme.colorScheme.error,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = { viewModel.loadDailyWeather() }) {
                                Text(stringResource(R.string.button_retry))
                            }
                        }
                    }
                }

                dailyDataList.isEmpty() -> {
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
                        itemsIndexed(dailyDataList) { index, dayData ->
                            val isExpanded = index == expandedIndex

                            val displayTime = if (index == 0) {
                                stringResource(R.string.text_today)
                            } else {
                                dayData.time
                            }

                            ExpandableDailyCard(
                                data = dayData,
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
fun ExpandableDailyCard(
    data: DailyWeatherData,
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
                    modifier = Modifier.weight(1.1f)
                )

                Text(
                    text = "${data.temperature2mMin}°C / ${data.temperature2mMax}°C",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1.4f),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "\uD83C\uDF27\uFE0F ${data.precipitationProbabilityMax}%",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(0.9f),
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

                    WeatherItem(stringResource(R.string.text_apparent_temperature_range), "${data.apparentTemperatureMin}°C / ${data.apparentTemperatureMax}°C")
                    WeatherItem(stringResource(R.string.text_uv_index), "${data.uvIndexMax}")
                    WeatherItem(stringResource(R.string.text_sunrise), data.sunrise)
                    WeatherItem(stringResource(R.string.text_sunset), data.sunset)
                    WeatherItem(stringResource(R.string.text_sunshine_duration), "${(data.sunshineDuration / 3600).toInt()} h")
                    WeatherItem(stringResource(R.string.text_daylight_duration), "${(data.daylightDuration / 3600).toInt()} h")
                    WeatherItem(stringResource(R.string.text_max_wind), "${data.windSpeed10mMax} km/h")
                    WeatherItem(stringResource(R.string.text_wind_direction), stringResource(id = viewModel.getWindDirectionResId(data.windDirection10mDominant)))
                    WeatherItem(stringResource(R.string.text_wind_gusts), "${data.windGusts10mMax} km/h")
                    WeatherItem(stringResource(R.string.text_precipitation_hours), "${data.precipitationHours} h")
                    WeatherItem(stringResource(R.string.text_shortwave_radiation), "${data.shortwaveRadiationSum} MJ/m²")

                    if (data.precipitationSum > 0.0) {
                        WeatherItem(stringResource(R.string.text_precipitation_sum), "${data.precipitationSum} mm")
                    }
                }
            }
        }
    }
}
