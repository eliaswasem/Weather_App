package com.elias.weatherapp.data.model.domain

data class CurrentWeatherData(
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Double,
    val precipitation: Double,
    val snowfall: Double,
    val cloudCover: Int,
    val pressureMsl: Double,
    val surfacePressure: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val windGusts: Double
)