package com.elias.weatherapp.apis

import com.elias.weatherapp.data.model.LocationApiResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationApi {

    @GET("search")
    suspend fun getLocation(
        @Query("city") name: String,
        @Query("country") country: String,
        @Query("count") count: Int = 1
    ): LocationApiResponse
}