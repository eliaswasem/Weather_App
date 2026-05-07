package com.elias.weatherapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.elias.weatherapp.ui.screens.HomeScreen
import com.elias.weatherapp.ui.screens.WelcomeScreen

object Routes {
    const val WELCOME = "welcome"
    const val HOME = "home"
    const val SETTINGS = "settings"
}

@Composable
fun WeatherNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.WELCOME,
        modifier = modifier
    ) {
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen()
        }

        composable(Routes.SETTINGS) {
            //SettingsScreen()
        }
    }
}