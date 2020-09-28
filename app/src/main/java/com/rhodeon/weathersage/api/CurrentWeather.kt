package com.rhodeon.weathersage.api

import com.squareup.moshi.Json

data class Forecast(
    val temp: Float,
    val pressure: Float,
    val humidity: Float
)

data class Wind(val speed: Float)

data class Coordinates(
    val lat: Float,
    val lon: Float
)

data class CurrentWeatherDescription(
    val description: String,
    val icon: String
)

data class Sys(val country: String)

data class CurrentWeather(
    // Model of each forecast with name, coordinate and temperature value
    @field:Json(name = "name") val locationName: String,
    val coord: Coordinates,
    @field:Json(name = "main") val forecast: Forecast,
    val wind: Wind,
    @field:Json(name = "dt") val date: Long,
    val weather: List<CurrentWeatherDescription>,
    val sys: Sys
)