package com.elias.weatherapp.data.mapper

import com.elias.weatherapp.data.apis.response.CurrentWeatherApiResponse
import com.elias.weatherapp.data.model.domain.CurrentWeatherData
fun CurrentWeatherApiResponse.toWeatherData(): CurrentWeatherData? {

    val currentDto = this.current ?: return null

    return CurrentWeatherData(
        temperature = currentDto.temperature,
        humidity = currentDto.humidity,
        apparentTemperature = currentDto.apparentTemperature,
        precipitation = currentDto.precipitation,
        snowfall = currentDto.snowfall,
        cloudCover = currentDto.cloudCover,
        pressureMsl = currentDto.pressureMsl,
        surfacePressure = currentDto.surfacePressure,
        windSpeed = currentDto.windSpeed,
        windDirection = currentDto.windDirection,
        windGusts = currentDto.windGusts
    )
}
