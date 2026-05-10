package com.elias.weatherapp.viewmodel

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
import com.elias.weatherapp.data.model.AppLanguage
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.LocationData
import com.elias.weatherapp.data.model.CurrentWeatherData
import com.elias.weatherapp.data.toWeatherData
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

    var isLoading by mutableStateOf(false)
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

    fun loadWeather() {
        viewModelScope.launch {
            isLoading = true
            val coords = saveHandler.getSavedLocation()

            if (coords != null) {
                cityName = coords.name
                try {
                    errorMessageResId = null

                    val response = RetrofitClient.weatherApi.getWeather(
                        coords.latitude,
                        coords.longitude
                    )
                    _weather.value = response.toWeatherData()
                } catch (e: Exception) {
                    _weather.value = null
                    errorMessageResId = R.string.error_failed_load_weather
                } finally {
                    isLoading = false
                }
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
        loadWeather()
    }

    fun updateLanguage(language: AppLanguage) {
        _language.value = language

        val localeList = LocaleListCompat.forLanguageTags(language.code)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

}