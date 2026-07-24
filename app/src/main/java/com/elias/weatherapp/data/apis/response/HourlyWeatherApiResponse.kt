package com.elias.weatherapp.data.apis.response

import com.google.gson.annotations.SerializedName

data class HourlyWeatherApiResponse(
    val hourly: HourlyWeatherDto?
)

data class HourlyWeatherDto(
    @SerializedName("time") val time: List<String>?,
    @SerializedName("temperature_2m") val temperature: List<Double>?,
    @SerializedName("relative_humidity_2m") val humidity: List<Int>?,
    @SerializedName("apparent_temperature") val apparentTemperature: List<Double>?,
    @SerializedName("precipitation_probability") val precipitationProbability: List<Int>?,
    @SerializedName("precipitation") val precipitation: List<Double>?,
    @SerializedName("snowfall") val snowfall: List<Double>?,
    @SerializedName("snow_depth") val snowDepth: List<Double>?,
    @SerializedName("surface_pressure") val surfacePressure: List<Double>?,
    @SerializedName("pressure_msl") val pressureMsl: List<Double>?,
    @SerializedName("cloud_cover") val cloudCover: List<Int>?,
    @SerializedName("visibility") val visibility: List<Double>?,
    @SerializedName("wind_speed_10m") val windSpeed: List<Double>?,
    @SerializedName("wind_direction_10m") val windDirection: List<Int>?,
    @SerializedName("wind_gusts_10m") val windGusts: List<Double>?,
    @SerializedName("soil_temperature_0cm") val soilTemperature: List<Double>?,
    @SerializedName("soil_moisture_0_to_1cm") val soilMoisture: List<Double>?
)
