package com.elias.weatherapp.data.mapper

import com.elias.weatherapp.data.apis.response.HourlyWeatherApiResponse
import com.elias.weatherapp.data.model.domain.HourlyWeatherData
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun HourlyWeatherApiResponse.toHourlyWeatherDataList(): List<HourlyWeatherData> {
    val dto = this.hourly ?: return emptyList()

    val times = dto.time ?: return emptyList()

    val currentDateTime = LocalDateTime.now()
        .withMinute(0)
        .withSecond(0)
        .withNano(0)

    val startIndex = times.indexOfFirst { apiTimeStr ->
        try {
            val apiDateTime = LocalDateTime.parse(apiTimeStr, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            !apiDateTime.isBefore(currentDateTime)
        } catch (_: Exception) {
            false
        }
    }.takeIf { it >= 0 } ?: 0

    val endIndex = (startIndex + 25).coerceAtMost(times.size)

    return (startIndex until endIndex).mapNotNull { index ->

        val time = times.getOrNull(index) ?: return@mapNotNull null

        HourlyWeatherData(
            time = time.substringAfter("T"),
            temperature = dto.temperature?.getOrNull(index) ?: return@mapNotNull null,
            humidity = dto.humidity?.getOrNull(index) ?: return@mapNotNull null,
            apparentTemperature = dto.apparentTemperature?.getOrNull(index) ?: return@mapNotNull null,
            precipitationProbability = dto.precipitationProbability?.getOrNull(index) ?: 0,
            precipitation = dto.precipitation?.getOrNull(index) ?: 0.0,
            snowfall = dto.snowfall?.getOrNull(index) ?: 0.0,
            snowDepth = dto.snowDepth?.getOrNull(index) ?: 0.0,
            surfacePressure = dto.surfacePressure?.getOrNull(index) ?: 0.0,
            pressureMsl = dto.pressureMsl?.getOrNull(index) ?: 0.0,
            cloudCover = dto.cloudCover?.getOrNull(index) ?: 0,
            visibility = (dto.visibility?.getOrNull(index)?.div(1000)) ?: 0.0,
            windSpeed = dto.windSpeed?.getOrNull(index) ?: 0.0,
            windDirection = dto.windDirection?.getOrNull(index) ?: 0,
            windGusts = dto.windGusts?.getOrNull(index) ?: 0.0,
            soilTemperature = dto.soilTemperature?.getOrNull(index) ?: 0.0,
            soilMoisture = dto.soilMoisture?.getOrNull(index) ?: 0.0
        )
    }
}