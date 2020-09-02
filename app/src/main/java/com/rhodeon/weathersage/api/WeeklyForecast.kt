package com.rhodeon.weathersage.api

import com.squareup.moshi.Json

// Employs use of the One Call API to display daily forecasts for a week

data class Temp (
    val min: Float,
    val max: Float
)

data class WeatherDescription (
    val main: String,
    val description: String,
    val icon: String
)

data class DailyForecast(
    @field:Json(name = "dt") val date: Long,
    val temp: Temp,
    val weather: List<WeatherDescription>

)

data class WeeklyForecast (val daily: List<DailyForecast>)