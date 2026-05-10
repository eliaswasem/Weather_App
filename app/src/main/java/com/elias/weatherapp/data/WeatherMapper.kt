package com.elias.weatherapp.data

import com.elias.weatherapp.data.model.WeatherApiResponse
import com.elias.weatherapp.data.model.CurrentWeatherData

fun WeatherApiResponse.toWeatherData(): CurrentWeatherData {
    return CurrentWeatherData(
        temperature = this.current.temperature,
        humidity = this.current.humidity,
        windSpeed = this.current.windSpeed
    )
}