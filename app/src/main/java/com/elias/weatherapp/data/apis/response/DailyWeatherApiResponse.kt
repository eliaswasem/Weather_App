package com.elias.weatherapp.data.apis.response

import com.google.gson.annotations.SerializedName

data class DailyWeatherApiResponse(
    val daily: DailyWeatherDto
)

data class DailyWeatherDto(
    @SerializedName("apparent_temperature_max") val apparentTemperatureMax: List<Double?>?,
    @SerializedName("apparent_temperature_min") val apparentTemperatureMin: List<Double?>?,
    @SerializedName("uv_index_max") val uvIndexMax: List<Double?>?,
    @SerializedName("sunshine_duration") val sunshineDuration: List<Double?>?,
    @SerializedName("daylight_duration") val daylightDuration: List<Double?>?,
    @SerializedName("sunrise") val sunrise: List<String?>?,
    @SerializedName("precipitation_sum") val precipitationSum: List<Double?>?,
    @SerializedName("precipitation_probability_max") val precipitationProbabilityMax: List<Int?>?,
    @SerializedName("precipitation_hours") val precipitationHours: List<Double?>?,
    @SerializedName("shortwave_radiation_sum") val shortwaveRadiationSum: List<Double?>?,
    @SerializedName("wind_speed_10m_max") val windSpeed10mMax: List<Double?>?,
    @SerializedName("wind_gusts_10m_max") val windGusts10mMax: List<Double?>?,
    @SerializedName("wind_direction_10m_dominant") val windDirection10mDominant: List<Int?>?,
    @SerializedName("sunset") val sunset: List<String?>?,
    @SerializedName("temperature_2m_max") val temperature2mMax: List<Double?>?,
    @SerializedName("temperature_2m_min") val temperature2mMin: List<Double?>?
)
