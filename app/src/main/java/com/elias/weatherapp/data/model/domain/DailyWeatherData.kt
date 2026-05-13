package com.elias.weatherapp.data.model.domain

data class DailyWeatherData(
    val time: String,
    val apparentTemperatureMax: Double,
    val apparentTemperatureMin: Double,
    val uvIndexMax: Double,
    val sunshineDuration: Double,
    val daylightDuration: Double,
    val sunrise: String,
    val precipitationSum: Double,
    val precipitationProbabilityMax: Int,
    val precipitationHours: Double,
    val shortwaveRadiationSum: Double,
    val windSpeed10mMax: Double,
    val windGusts10mMax: Double,
    val windDirection10mDominant: Int,
    val sunset: String,
    val temperature2mMax: Double,
    val temperature2mMin: Double
)
