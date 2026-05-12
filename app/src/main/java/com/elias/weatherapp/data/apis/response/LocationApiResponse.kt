package com.elias.weatherapp.data.apis.response

import com.google.gson.annotations.SerializedName

data class LocationApiResponse(
    @SerializedName("lat") val latitude: Double,
    @SerializedName("lon") val longitude: Double,
    @SerializedName("display_name") val displayName: String,
    @SerializedName("address") val address: Address?
) {
    val country: String?
        get() = address?.country ?: displayName.split(",").lastOrNull()?.trim()

    val name: String
        get() {
            val city = address?.city
                ?: address?.town
                ?: address?.village
                ?: address?.municipality
                ?: displayName.split(",").first().trim()

            return if (country != null) "$city, $country" else city
        }
}

data class Address(
    val city: String?,
    val town: String?,
    val village: String?,
    val municipality: String?,
    val country: String?
)
