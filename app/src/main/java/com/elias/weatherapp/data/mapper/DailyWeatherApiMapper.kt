package com.elias.weatherapp.data.mapper

import com.elias.weatherapp.data.apis.response.DailyWeatherApiResponse
import com.elias.weatherapp.data.model.domain.DailyWeatherData
import com.elias.weatherapp.getLanguageCode
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun DailyWeatherApiResponse.toDailyWeatherDataList(): List<DailyWeatherData> {
    val dto = this.daily ?: return emptyList()

    val times = dto.sunrise ?: return emptyList()

    val currentDate = LocalDate.now()

    val currentLocale = Locale.forLanguageTag(getLanguageCode())

    val startIndex = times.indexOfFirst { apiTimeStr ->
        try {
            val dateStr = apiTimeStr?.substringBefore("T") ?: return@indexOfFirst false
            val apiDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
            !apiDate.isBefore(currentDate)
        } catch (_: Exception) {
            false
        }
    }.takeIf { it >= 0 } ?: 0

    val endIndex = (startIndex + 8).coerceAtMost(times.size)

    return (startIndex until endIndex).mapNotNull { index ->
        val fullTime = times.getOrNull(index) ?: return@mapNotNull null
        val dateStr = fullTime.substringBefore("T")

        val dayName = try {
            val apiDate = LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE)
            apiDate.dayOfWeek.getDisplayName(TextStyle.FULL, currentLocale)
        } catch (_: Exception) {
            dateStr
        }

        DailyWeatherData(
            time = dayName,
            apparentTemperatureMax = dto.apparentTemperatureMax?.getOrNull(index) ?: return@mapNotNull null,
            apparentTemperatureMin = dto.apparentTemperatureMin?.getOrNull(index) ?: return@mapNotNull null,
            uvIndexMax = dto.uvIndexMax?.getOrNull(index) ?: return@mapNotNull null,
            sunshineDuration = dto.sunshineDuration?.getOrNull(index) ?: 0.0,
            daylightDuration = dto.daylightDuration?.getOrNull(index) ?: 0.0,
            sunrise = dto.sunrise.getOrNull(index)?.substringAfter("T") ?: "--:--",
            precipitationSum = dto.precipitationSum?.getOrNull(index) ?: 0.0,
            precipitationProbabilityMax = dto.precipitationProbabilityMax?.getOrNull(index) ?: 0,
            precipitationHours = dto.precipitationHours?.getOrNull(index) ?: 0.0,
            shortwaveRadiationSum = dto.shortwaveRadiationSum?.getOrNull(index) ?: 0.0,
            windSpeed10mMax = dto.windSpeed10mMax?.getOrNull(index) ?: 0.0,
            windGusts10mMax = dto.windGusts10mMax?.getOrNull(index) ?: 0.0,
            windDirection10mDominant = dto.windDirection10mDominant?.getOrNull(index) ?: 0,
            sunset = dto.sunset?.getOrNull(index)?.substringAfter("T") ?: "--:--",
            temperature2mMax = dto.temperature2mMax?.getOrNull(index) ?: return@mapNotNull null,
            temperature2mMin = dto.temperature2mMin?.getOrNull(index) ?: return@mapNotNull null
        )
    }
}
