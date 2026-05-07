package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@Composable
fun WelcomeScreen(
    viewModel: WeatherAppViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {

    Column {

        Button(onClick = {
            viewModel.loadWeatherByCity("Berlin", "Germany")
        }) {
            Text("Get Weather")
        }

        val weather = viewModel.weather.value

        weather?.let {
            Text("Temp: ${it.temperature}")
            Text("Humidity: ${it.humidity}")
            Text("Wind: ${it.windSpeed}")
        }
    }
}