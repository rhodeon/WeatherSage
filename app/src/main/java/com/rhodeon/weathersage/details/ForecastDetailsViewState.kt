package com.rhodeon.weathersage.details


data class ForecastDetailsViewState(
    val maxTemp: Float,
    val minTemp: Float,
    val tempDescription: String,
    val iconId: String
)