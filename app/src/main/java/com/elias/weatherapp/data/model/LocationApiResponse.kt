package com.elias.weatherapp.data.model

data class LocationApiResponse(
    val results: List<LocationResult>
)

data class LocationResult(
    val name: String,
    val country: String,
    val latitude: Double,
    val longitude: Double
)