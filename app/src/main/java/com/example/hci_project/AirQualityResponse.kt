package com.example.hci_project



data class AirQualityResponse(
    val list: List<AirQualityData>
)

data class AirQualityData(
    val main: AirQualityMain,
    val components: Components
)

data class AirQualityMain(
    val aqi: Int
)

data class Components(
    val co: Double,
    val no2: Double,
    val o3: Double,
    val pm10: Double,
    val pm2_5: Double,
    val so2: Double,

)
