package com.elias.weatherapp.data.apis.response

import com.google.gson.annotations.SerializedName

data class CurrentWeatherApiResponse(
    val current: CurrentWeatherDto?
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("relative_humidity_2m") val humidity: Int,
    @SerializedName("apparent_temperature") val apparentTemperature: Double,
    @SerializedName("precipitation") val precipitation: Double,
    @SerializedName("snowfall") val snowfall: Double,
    @SerializedName("cloud_cover") val cloudCover: Int,
    @SerializedName("pressure_msl") val pressureMsl: Double,
    @SerializedName("surface_pressure") val surfacePressure: Double,
    @SerializedName("wind_speed_10m") val windSpeed: Double,
    @SerializedName("wind_direction_10m") val windDirection: Int,
    @SerializedName("wind_gusts_10m") val windGusts: Double
)