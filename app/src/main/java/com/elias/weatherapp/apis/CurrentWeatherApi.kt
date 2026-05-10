package com.elias.weatherapp.apis

import com.elias.weatherapp.data.model.CurrentWeatherApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApi {

    @GET("forecast")
    suspend fun getWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("current") current: String =
            "temperature_2m,relative_humidity_2m,wind_speed_10m"
    ): CurrentWeatherApiResponse
}