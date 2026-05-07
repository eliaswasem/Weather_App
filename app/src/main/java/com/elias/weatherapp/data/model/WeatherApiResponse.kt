package com.elias.weatherapp.data.model

data class WeatherApiResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    val temperature_2m: Double,
    val relative_humidity_2m: Double,
    val wind_speed_10m: Double
)