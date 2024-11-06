package com.example.hci_project


import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface Service{
    @GET("data/2.5/air_pollution")
    fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): Call<AirQualityResponse>
}
