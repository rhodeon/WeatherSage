package com.rhodeon.weathersage.api

import com.squareup.moshi.Json

data class Forecast(val temp: Float)

data class Coordinates(
    val lat: Float,
    val lon: Float
)

data class CurrentWeatherDescription(
    val description: String,
    val icon: String
)

data class CurrentWeather(
    // Model of each forecast with name, coordinate and temperature value
    @field:Json(name = "name") val locationName: String,
    val coord: Coordinates,
    @field:Json(name = "main") val forecast: Forecast,
    @field:Json(name = "dt") val date: Long,
    val weather: List<CurrentWeatherDescription>
)