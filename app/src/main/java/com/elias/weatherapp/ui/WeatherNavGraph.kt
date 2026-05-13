package com.elias.weatherapp.ui

import com.elias.weatherapp.ui.screens.WeekWeatherScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elias.weatherapp.ui.screens.DayWeatherScreen
import com.elias.weatherapp.ui.screens.HomeScreen
import com.elias.weatherapp.ui.screens.LocationInputScreen
import com.elias.weatherapp.ui.screens.SettingsScreen
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

object Routes {
    const val WELCOME = "welcome"
    const val HOME = "home"
    const val SETTINGS = "settings"
    const val DAY = "day"
    const val WEEK = "week"
    const val LOCATION_INPUT = "location_input"
}

@Composable
fun WeatherNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: WeatherAppViewModel = hiltViewModel()
) {

    var startRoute by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        val location = viewModel.getSavedLocation()
        startRoute = if (location == null) Routes.WELCOME else Routes.HOME
    }

    if (startRoute == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        NavHost(
            navController = navController,
            startDestination = startRoute!!,
            modifier = modifier
        ) {
            composable(Routes.WELCOME) {
                LocationInputScreen(
                    onNavigateToHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.WELCOME) { inclusive = true }
                        }
                    },
                    isWelcome = true
                )
            }

            composable(Routes.HOME) {
                HomeScreen(
                    onLocationChange = {
                        navController.navigate(Routes.LOCATION_INPUT) {
                            popUpTo(0) { inclusive = true}
                        }
                    }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsScreen(
                )
            }

            composable(Routes.DAY) {
                DayWeatherScreen()
            }

            composable(Routes.WEEK) {
                WeekWeatherScreen()
            }

            composable(Routes.LOCATION_INPUT) {
                LocationInputScreen(
                    onNavigateToHome = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.WELCOME) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
