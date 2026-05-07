package com.elias.weatherapp.data

import com.elias.weatherapp.data.model.WeatherApiResponse
import com.elias.weatherapp.data.model.WeatherData

fun WeatherApiResponse.toWeatherData(): WeatherData {
    return WeatherData(
        temperature = this.current.temperature,
        humidity = this.current.humidity,
        windSpeed = this.current.windSpeed
    )
}