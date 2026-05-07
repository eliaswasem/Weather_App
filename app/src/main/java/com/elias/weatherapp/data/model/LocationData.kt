package com.elias.weatherapp.data.model

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null,
    val country: String? = null
)