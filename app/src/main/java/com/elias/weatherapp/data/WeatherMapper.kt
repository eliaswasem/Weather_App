package com.elias.weatherapp.data

import com.elias.weatherapp.data.model.WeatherApiResponse
import com.elias.weatherapp.data.model.WeatherData

fun WeatherApiResponse.toWeatherData(): WeatherData {
    return WeatherData(
        temperature = current.temperature_2m,
        humidity = current.relative_humidity_2m,
        windSpeed = current.wind_speed_10m
    )
}