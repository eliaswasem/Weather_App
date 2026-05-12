package com.elias.weatherapp.data.model.domain

data class HourlyWeatherData(
    val time: String,
    val temperature: Double,
    val humidity: Int,
    val apparentTemperature: Double,
    val precipitationProbability: Int,
    val precipitation: Double,
    val snowfall: Double,
    val snowDepth: Double,
    val surfacePressure: Double,
    val pressureMsl: Double,
    val cloudCover: Int,
    val visibility: Double,
    val windSpeed: Double,
    val windDirection: Int,
    val windGusts: Double,
    val soilTemperature: Double,
    val soilMoisture: Double
)
