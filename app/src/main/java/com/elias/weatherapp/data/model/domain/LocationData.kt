package com.elias.weatherapp.data.model.domain

data class LocationData(
    val latitude: Double,
    val longitude: Double,
    val name: String? = null,
    val country: String? = null
)