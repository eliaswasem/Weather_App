package com.elias.weatherapp.data.model

data class LocationApiResponse(
    val results: List<LocationResult>
)

data class LocationResult(
    val latitude: Double,
    val longitude: Double
)