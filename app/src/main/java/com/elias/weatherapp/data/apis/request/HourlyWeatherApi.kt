package com.elias.weatherapp.data.apis.request

import com.elias.weatherapp.data.apis.response.HourlyWeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface HourlyWeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("hourly") hourly: String = "temperature_2m,relative_humidity_2m,apparent_temperature,precipitation_probability,precipitation,snowfall,snow_depth,surface_pressure,pressure_msl,cloud_cover,visibility,wind_speed_10m,wind_direction_10m,wind_gusts_10m,soil_temperature_0cm,soil_moisture_0_to_1cm",
        @Query("forecast_days") forecastDays: Int = 2,
        @Query("timezone") timezone: String = "auto"
    ): HourlyWeatherApiResponse
}