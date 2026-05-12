package com.elias.weatherapp.viewmodel

import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.weatherapp.R
import com.elias.weatherapp.RetrofitClient
import com.elias.weatherapp.data.SettingsSaveHandler
import com.elias.weatherapp.data.mapper.toHourlyWeatherDataList
import com.elias.weatherapp.data.model.AppLanguage
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.domain.LocationData
import com.elias.weatherapp.data.model.domain.CurrentWeatherData
import com.elias.weatherapp.data.model.domain.DisplaySettings
import com.elias.weatherapp.data.mapper.toWeatherData
import com.elias.weatherapp.data.model.domain.HourlyWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherAppViewModel @Inject constructor(
    private val saveHandler: SettingsSaveHandler
) : ViewModel()  {

    private val _weather = MutableStateFlow<CurrentWeatherData?>(null)
    val weather = _weather.asStateFlow()

    private val _hourlyWeather = MutableStateFlow<List<HourlyWeatherData>>(emptyList())
    val hourlyWeather = _hourlyWeather.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var isHourlyLoading by mutableStateOf(false)
        private set

    var hourlyErrorOccurred by mutableStateOf(false)
        private set

    var errorMessageResId by mutableStateOf<Int?>(null)
        private set

    var cityName by mutableStateOf<String?>(null)
        private set

    private val _language = MutableStateFlow(
        AppLanguage.fromCode(
            AppCompatDelegate.getApplicationLocales()
                .get(0)
                ?.language
        )
    )

    val language = _language.asStateFlow()

    fun loadCurrentWeather(isBackgroundUpdate: Boolean = false) {
        viewModelScope.launch {
            if (!isBackgroundUpdate) {
                isLoading = true
            }

            val coords = saveHandler.getSavedLocation()

            if (coords != null) {
                cityName = coords.name
                try {
                    errorMessageResId = null
                    val response = RetrofitClient.currentWeatherApi.getWeather(
                        coords.latitude,
                        coords.longitude
                    )
                    _weather.value = response.toWeatherData()
                } catch (e: Exception) {
                    if (_weather.value == null) {
                        _weather.value = null
                    }
                    errorMessageResId = R.string.error_failed_load_weather
                } finally {
                    isLoading = false
                }
            } else {
                isLoading = false
            }
        }
    }


    fun loadHourlyWeather(force: Boolean = false) {
        viewModelScope.launch {

            if (!force && _hourlyWeather.value.isNotEmpty()) return@launch

            isHourlyLoading = true
            hourlyErrorOccurred = false

            val coords = saveHandler.getSavedLocation()

            if (coords == null) {
                isHourlyLoading = false
                return@launch
            }

            try {
                val response = RetrofitClient.hourlyWeatherApi.getWeather(
                    coords.latitude,
                    coords.longitude
                )

                _hourlyWeather.value = response.toHourlyWeatherDataList()

            } catch (e: Exception) {
                hourlyErrorOccurred = true
                _hourlyWeather.value = emptyList()
            } finally {
                isHourlyLoading = false
            }
        }
    }

    fun getAndSaveLocationFromCoords(city: String, country: String, onSuccess: () -> Unit) {
        if (city.isBlank()) {
            errorMessageResId = R.string.error_city_empty
            return
        }
        if (country.isBlank()) {
            errorMessageResId = R.string.error_country_empty
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessageResId = null

                val responseList = RetrofitClient.locationApi.getLocation(city.trim(), country.trim())
                val location = responseList.firstOrNull()

                if (location != null) {
                    saveHandler.saveLocation(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            name = location.name
                        )
                    )
                    onSuccess()
                } else {
                    errorMessageResId = R.string.error_location_not_found
                }
            } catch (e: Exception) {
                errorMessageResId = R.string.error_network_connection_failed
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun getSavedLocation(): LocationData? {
        return saveHandler.getSavedLocation()
    }

    fun deleteLocationAndReset() {
        viewModelScope.launch {
            saveHandler.clearLocation()

            _weather.value = null
            _hourlyWeather.value = emptyList()

            hourlyErrorOccurred = false
        }
    }
    val theme: StateFlow<AppTheme> = saveHandler.themeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = AppTheme.SYSTEM
        )
    fun updateTheme(newTheme: AppTheme) {
        viewModelScope.launch {
            saveHandler.saveTheme(newTheme)
        }
    }
    fun retry() {
        errorMessageResId = null
        loadCurrentWeather()
    }

    fun updateLanguage(language: AppLanguage) {
        _language.value = language

        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }
    fun getWindDirectionResId(degree: Int): Int {
        return when (degree) {
            in 338..360, in 0..22 -> R.string.wind_n
            in 23..67 -> R.string.wind_ne
            in 68..112 -> R.string.wind_e
            in 113..157 -> R.string.wind_se
            in 158..202 -> R.string.wind_s
            in 203..247 -> R.string.wind_sw
            in 248..292 -> R.string.wind_w
            in 293..337 -> R.string.wind_nw
            else -> R.string.unknown
        }
    }

    fun toggleDisplaySetting(key: String, isEnabled: Boolean) {
        viewModelScope.launch {
            saveHandler.updateDisplaySetting(key, isEnabled)
        }
    }

    val displaySettings = saveHandler.displaySettingsFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DisplaySettings())

}