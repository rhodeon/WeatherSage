package com.rhodeon.weathersage.ui.weeklyforecastdetails


data class ForecastDetailsViewState(
    val maxTemp: Float,
    val minTemp: Float,
    val tempDescription: String,
    val iconId: String
)