package com.elias.weatherapp.data.apis.request

import com.elias.weatherapp.data.apis.response.DailyWeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface DailyWeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("daily") daily: String = "apparent_temperature_max,apparent_temperature_min,uv_index_max,sunshine_duration,daylight_duration,sunrise,precipitation_sum,precipitation_probability_max,precipitation_hours,shortwave_radiation_sum,wind_speed_10m_max,wind_gusts_10m_max,wind_direction_10m_dominant,sunset,temperature_2m_max,temperature_2m_min",
        @Query("forecast_days") forecastDays: Int = 8,
        @Query("timezone") timezone: String = "auto"
    ): DailyWeatherApiResponse
}