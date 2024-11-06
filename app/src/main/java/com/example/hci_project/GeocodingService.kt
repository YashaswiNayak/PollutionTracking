package com.example.hci_project

// GeocodingService.kt

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GeocodingService {
    @GET("geo/1.0/reverse")
    fun getCityNameFromCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("limit") limit: Int = 1
    ): Call<List<GeocodeResponse>>
}
