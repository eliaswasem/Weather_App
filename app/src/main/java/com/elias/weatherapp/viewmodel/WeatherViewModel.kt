package com.elias.weatherapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elias.weatherapp.RetrofitClient
import com.elias.weatherapp.data.model.WeatherData
import com.elias.weatherapp.data.toWeatherData
import kotlinx.coroutines.launch

class WeatherAppViewModel : ViewModel() {

    var weather = mutableStateOf<WeatherData?>(null)
        private set

    fun loadWeatherByCity(city: String, country: String) {
        viewModelScope.launch {

            val locationResponse =
                RetrofitClient.locationApi.getLocation(city, country)

            val location = locationResponse.results.firstOrNull()
                ?: return@launch

            val weatherResponse =
                RetrofitClient.weatherApi.getWeather(
                    location.latitude,
                    location.longitude
                )

            weather.value = weatherResponse.toWeatherData()
        }
    }
}