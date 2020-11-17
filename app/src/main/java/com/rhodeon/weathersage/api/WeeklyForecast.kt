package com.rhodeon.weathersage.api

import android.os.Parcelable
import com.squareup.moshi.Json
import kotlinx.android.parcel.Parcelize

// Employs use of the One Call API to display daily forecasts for a week
@Parcelize
data class Temp (
    val min: Float,
    val max: Float
) : Parcelable

@Parcelize
data class WeatherDescription (
    val main: String,
    val description: String,
    val icon: String
) : Parcelable

@Parcelize
data class DailyForecast(
    @field:Json(name = "dt") val date: Long,
    val temp: Temp,
    val weather: List<WeatherDescription>
) : Parcelable

data class WeeklyForecast (val daily: List<DailyForecast>)