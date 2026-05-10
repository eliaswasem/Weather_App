package com.elias.weatherapp

import com.elias.weatherapp.apis.CurrentWeatherApi
import com.elias.weatherapp.apis.LocationApi
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private val nominatimHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header("User-Agent", "EliasWeatherApp/1.3.0 (ewasem@@outlook.de)")
                .build()
            chain.proceed(request)
        }
        .build()

    val currentWeatherApi: CurrentWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CurrentWeatherApi::class.java)
    }

    val locationApi: LocationApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://nominatim.openstreetmap.org/")
            .client(nominatimHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationApi::class.java)
    }
}
