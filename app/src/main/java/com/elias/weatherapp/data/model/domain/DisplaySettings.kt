package com.elias.weatherapp.data.model.domain

data class DisplaySettings(
    val showWind: Boolean = true,
    val showApparentTemp: Boolean = true,
    val showWindGusts: Boolean = true,
    val showHumidity: Boolean = true,
    val showCloudCover: Boolean = true,
    val showPrecipitation: Boolean = true,
    val showPressureMsl: Boolean = true,
    val showSurfacePressure: Boolean = true,
    val showWindDirection: Boolean = true,
    val showSnowfall: Boolean = true
)