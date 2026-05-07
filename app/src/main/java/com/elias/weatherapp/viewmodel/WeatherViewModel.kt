package com.elias.weatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.weatherapp.RetrofitClient
import com.elias.weatherapp.data.SettingsSaveHandler
import com.elias.weatherapp.data.model.WeatherData
import com.elias.weatherapp.data.toWeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WeatherAppViewModel(
    private val saveHandler: SettingsSaveHandler
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherData?>(null)
    val weather = _weather.asStateFlow()

    fun loadWeather() {
        viewModelScope.launch {
            val coords = saveHandler.getSavedLocation() ?: return@launch

            val (lat, lon) = coords

            val response = try {
                RetrofitClient.weatherApi.getWeather(lat, lon)
            } catch (e: Exception) {
                null
            }

            _weather.value = response?.toWeatherData()
        }
    }

    fun saveLocation(lat: Double, lon: Double) {
        viewModelScope.launch {
            saveHandler.saveLocation(lat, lon)
        }
    }

    fun getAndSaveLocationFromCoords(city: String, country: String) {
        viewModelScope.launch {

            val response = try {
                RetrofitClient.locationApi.getLocation(city, country)
            } catch (e: Exception) {
                null
            }

            val location = response?.results?.firstOrNull() ?: return@launch

            saveHandler.saveLocation(
                lat = location.latitude,
                lon = location.longitude
            )
        }
    }
}