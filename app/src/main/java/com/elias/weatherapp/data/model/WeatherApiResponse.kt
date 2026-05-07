package com.elias.weatherapp.data.model

import com.google.gson.annotations.SerializedName

data class WeatherApiResponse(
    val current: CurrentWeather
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int, // Neu
    @SerializedName("wind_speed_10m") val windSpeed: Double    // Neu
)