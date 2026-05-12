package com.elias.weatherapp.data.apis.request

import com.elias.weatherapp.data.apis.response.CurrentWeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,is_day,precipitation,snowfall,cloud_cover,pressure_msl,surface_pressure,wind_speed_10m,wind_direction_10m,wind_gusts_10m",
        @Query("timezone") timezone: String = "auto"
    ): CurrentWeatherApiResponse
}