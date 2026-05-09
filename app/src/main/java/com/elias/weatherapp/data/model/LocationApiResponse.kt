package com.elias.weatherapp.data.model

data class LocationApiResponse(
    val results: List<LocationResult>? = null,
    val generationtime_ms: Double
)


data class LocationResult(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val country: String? = null,
)