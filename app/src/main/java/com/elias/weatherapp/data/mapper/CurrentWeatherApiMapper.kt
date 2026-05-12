package com.elias.weatherapp.data.mapper

import com.elias.weatherapp.data.apis.response.CurrentWeatherApiResponse
import com.elias.weatherapp.data.model.domain.CurrentWeatherData

fun CurrentWeatherApiResponse.toWeatherData(): CurrentWeatherData {
    return CurrentWeatherData(
        temperature = this.current.temperature,
        humidity = this.current.humidity,
        apparentTemperature = this.current.apparentTemperature,
        precipitation = this.current.precipitation,
        snowfall = this.current.snowfall,
        cloudCover = this.current.cloudCover,
        pressureMsl = this.current.pressureMsl,
        surfacePressure = this.current.surfacePressure,
        windSpeed = this.current.windSpeed,
        windDirection = this.current.windDirection,
        windGusts = this.current.windGusts
    )
}
