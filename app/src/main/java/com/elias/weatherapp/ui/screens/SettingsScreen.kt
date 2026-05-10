package com.elias.weatherapp.ui.screens

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.elias.weatherapp.R
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.AppLanguage
import com.elias.weatherapp.viewmodel.WeatherAppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: WeatherAppViewModel = hiltViewModel(),
    onLocationDeleted: () -> Unit = {}
) {
    val currentTheme by viewModel.theme.collectAsState()

    var themeExpanded by remember { mutableStateOf(false) }
    var languageExpanded by remember { mutableStateOf(false) }

    val currentLanguage by viewModel.language.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(id = R.string.text_appearance),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = themeExpanded,
            onExpandedChange = { themeExpanded = !themeExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(id = currentTheme.labelResId),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.label_app_theme)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = themeExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = themeExpanded,
                onDismissRequest = { themeExpanded = false }
            ) {
                AppTheme.entries.forEach { themeOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(id = themeOption.labelResId)) },
                        onClick = {
                            viewModel.updateTheme(themeOption)
                            themeExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ExposedDropdownMenuBox(
            expanded = languageExpanded,
            onExpandedChange = { languageExpanded = !languageExpanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = stringResource(id = currentLanguage.labelResId),
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(id = R.string.label_app_language)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = languageExpanded) },
                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = languageExpanded,
                onDismissRequest = { languageExpanded = false }
            ) {
                AppLanguage.entries.forEach { languageOption ->
                    DropdownMenuItem(
                        text = { Text(stringResource(id = languageOption.labelResId)) },
                        onClick = {
                            viewModel.updateLanguage(languageOption)
                            languageExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        HorizontalDivider(modifier = Modifier.padding(bottom = 24.dp))

        Button(
            onClick = {
                viewModel.deleteLocationAndReset()
                onLocationDeleted()
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Delete, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = stringResource(id = R.string.button_delete_saved_location))
        }

        Text(
            text = stringResource(id = R.string.text_reset),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}