package com.example.hci_project

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object GeocodingClient {
    private const val BASE_URL = "https://api.openweathermap.org/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val geocodingService: GeocodingService by lazy {
        retrofit.create(GeocodingService::class.java)
    }
}
