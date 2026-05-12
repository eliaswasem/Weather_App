package com.elias.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cloud
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.elias.weatherapp.ui.Routes
import com.elias.weatherapp.ui.WeatherNavGraph
import com.elias.weatherapp.ui.theme.WeatherAppTheme
import com.elias.weatherapp.viewmodel.WeatherAppViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: WeatherAppViewModel = hiltViewModel()
            val selectedTheme by viewModel.theme.collectAsState()

            WeatherAppTheme(selectedTheme = selectedTheme) {
                val navController = rememberNavController()

                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != Routes.WELCOME) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentRoute == Routes.SETTINGS,
                                    onClick = {
                                        if (currentRoute != Routes.SETTINGS) {
                                            navController.navigate(Routes.SETTINGS) {
                                                launchSingleTop = true
                                            }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                    label = { Text(stringResource(R.string.label_settings)) }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == Routes.TODAY,
                                    onClick = {
                                        if (currentRoute != Routes.TODAY) {
                                            navController.navigate(Routes.TODAY) {
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Cloud, contentDescription = "Today")},
                                    label = { Text(stringResource(R.string.label_today))}
                                )
                                NavigationBarItem(
                                    selected = currentRoute == Routes.HOME,
                                    onClick = {
                                        if (currentRoute != Routes.HOME) {
                                            navController.navigate(Routes.HOME) {
                                                popUpTo(Routes.WELCOME) {
                                                    inclusive = false
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text(stringResource(R.string.label_home)) }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    WeatherNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
