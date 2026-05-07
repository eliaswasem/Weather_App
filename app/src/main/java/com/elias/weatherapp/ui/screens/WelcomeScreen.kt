package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@Composable
fun WelcomeScreen(
    viewModel: WeatherAppViewModel = viewModel(),
    onNavigateToHome: () -> Unit
) {

    var city by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }

    Column {

        TextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") }
        )

        TextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("Country") }
        )

        Button(onClick = {

            viewModel.getAndSaveLocationFromCoords(city, country)
            onNavigateToHome()
        }) {
            Text("Continue")
        }
    }
}