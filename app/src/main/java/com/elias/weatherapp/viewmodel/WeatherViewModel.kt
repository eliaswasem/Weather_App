package com.elias.weatherapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.weatherapp.RetrofitClient
import com.elias.weatherapp.data.SettingsSaveHandler
import com.elias.weatherapp.data.model.AppTheme
import com.elias.weatherapp.data.model.LocationData
import com.elias.weatherapp.data.model.WeatherData
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

    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather = _weather.asStateFlow()

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var cityName by mutableStateOf<String?>(null)
        private set

    fun loadWeather() {
        viewModelScope.launch {
            isLoading = true
            val coords = saveHandler.getSavedLocation()

            if (coords != null) {
                cityName = coords.name
                try {
                    errorMessage = null

                    val response = RetrofitClient.weatherApi.getWeather(
                        coords.latitude,
                        coords.longitude
                    )
                    _weather.value = response.toWeatherData()
                } catch (e: Exception) {
                    _weather.value = null
                    errorMessage = "Failed to load weather"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun getAndSaveLocationFromCoords(city: String, country: String, onSuccess: () -> Unit) {
        if (city.isBlank()) {
            errorMessage = "City cannot be empty"
            return
        }
        if (country.isBlank()) {
            errorMessage = "Country cannot be empty"
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null

                val response = RetrofitClient.locationApi.getLocation(city.trim(), country.trim())
                val location = response.results?.firstOrNull()

                if (location != null) {
                    saveHandler.saveLocation(
                        LocationData(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            name = location.name,
                            country = location.country
                        )
                    )
                    onSuccess()
                } else {
                    errorMessage = "Location not found. Check spelling."
                }
            } catch (e: Exception) {
                errorMessage = "Network connection failed"
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
        errorMessage = null
        loadWeather()
    }

}