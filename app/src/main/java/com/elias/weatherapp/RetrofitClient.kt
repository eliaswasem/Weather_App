package com.elias.weatherapp

import com.elias.weatherapp.data.apis.request.CurrentWeatherApi
import com.elias.weatherapp.data.apis.request.HourlyWeatherApi
import com.elias.weatherapp.data.apis.request.LocationApi
import com.elias.weatherapp.data.model.domain.HourlyWeatherData
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private val nominatimHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val request = chain.request().newBuilder()
                .header(
                    "User-Agent",
                    "EliasWeatherApp/${BuildConfig.VERSION_NAME}(ewasem@@outlook.de)"
                )
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

    val hourlyWeatherApi: HourlyWeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(HourlyWeatherApi::class.java)
    }
}

