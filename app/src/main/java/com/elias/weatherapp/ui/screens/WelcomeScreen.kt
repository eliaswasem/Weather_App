package com.elias.weatherapp.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNavigateToHome: () -> Unit = {}
) {
    Text(
        text = "Hi",
        modifier = modifier
    )
}