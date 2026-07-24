package com.elias.weatherapp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.elias.weatherapp.BuildConfig
import com.elias.weatherapp.R
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.AppLanguage
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: WeatherAppViewModel = hiltViewModel()
) {
    val currentTheme by viewModel.theme.collectAsState()
    val currentLanguage by viewModel.language.collectAsState()
    val displaySettings by viewModel.displaySettings.collectAsStateWithLifecycle()

    var themeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = stringResource(R.string.text_appearance),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- Theme Dropdown ---
        ExposedDropdownMenuBox(
            expanded = themeExpanded,
            onExpandedChange = { themeExpanded = !themeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(currentTheme.labelResId),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_app_theme)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = themeExpanded, onDismissRequest = { themeExpanded = false }) {
                AppTheme.entries.forEach { themeOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(themeOption.labelResId)) },
                        onClick = { viewModel.updateTheme(themeOption); themeExpanded = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- Language Dropdown ---
        ExposedDropdownMenuBox(
            expanded = languageExpanded,
            onExpandedChange = { languageExpanded = !languageExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(id = currentLanguage.labelResId),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.label_app_language)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth()
            )
            ExposedDropdownMenu(expanded = languageExpanded, onDismissRequest = { languageExpanded = false }) {
                AppLanguage.entries.forEach { languageOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(languageOption.labelResId)) },
                        onClick = { viewModel.updateLanguage(languageOption); languageExpanded = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.label_display_options),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        DisplaySwitch(
            label = stringResource(R.string.text_apparent_temperature),
            isOn = displaySettings.showApparentTemp,
            onToggle = { viewModel.toggleDisplaySetting("show_apparent_temp", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_wind),
            isOn = displaySettings.showWind,
            onToggle = { viewModel.toggleDisplaySetting("show_wind", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_wind_gusts),
            isOn = displaySettings.showWindGusts,
            onToggle = { viewModel.toggleDisplaySetting("show_wind_gusts", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_wind_direction),
            isOn = displaySettings.showWindDirection,
            onToggle = { viewModel.toggleDisplaySetting("show_wind_direction", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_humidity),
            isOn = displaySettings.showHumidity,
            onToggle = { viewModel.toggleDisplaySetting("show_humidity", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_cloud_cover),
            isOn = displaySettings.showCloudCover,
            onToggle = { viewModel.toggleDisplaySetting("show_cloud_cover", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_precipitation),
            isOn = displaySettings.showPrecipitation,
            onToggle = { viewModel.toggleDisplaySetting("show_precipitation", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_snowfall),
            isOn = displaySettings.showSnowfall,
            onToggle = { viewModel.toggleDisplaySetting("show_snowfall", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_pressure_msl),
            isOn = displaySettings.showPressureMsl,
            onToggle = { viewModel.toggleDisplaySetting("show_pressure_msl", it) }
        )
        DisplaySwitch(
            label = stringResource(R.string.text_surface_pressure),
            isOn = displaySettings.showSurfacePressure,
            onToggle = { viewModel.toggleDisplaySetting("show_surface_pressure", it) }
        )

        Spacer(modifier = Modifier.height(32.dp))
        HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp))

        Text(
            text = stringResource(R.string.label_app_info),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "${stringResource(R.string.text_app_version)}: ${BuildConfig.VERSION_NAME}",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "${stringResource(R.string.text_developer)}: Elias Wasem",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun DisplaySwitch(
    label: String,
    isOn: Boolean,
    enabled: Boolean = true,
    onToggle: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
        Switch(checked = isOn, onCheckedChange = onToggle, enabled = enabled)
    }
}
