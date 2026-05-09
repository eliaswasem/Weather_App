package com.elias.weatherapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@Composable
fun SettingsScreen(
    viewModel: WeatherAppViewModel = hiltViewModel(),
    onNavigateToHome: () -> Unit
) {
    Text(
        text = "Settings"
    )
}