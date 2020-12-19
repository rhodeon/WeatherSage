package com.rhodeon.weathersage.api

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

fun createOpenWeatherMapService() : OpenWeatherMapService {
    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.openweathermap.org")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    return retrofit.create(OpenWeatherMapService::class.java)
}

interface OpenWeatherMapService {
//    Current Weather API request
    @GET("/data/2.5/weather")
    suspend fun currentWeatherByName (
        @Query("q") location: String,
        @Query("units") unit: String,
        @Query("appid") apikey: String
    ): Response<CurrentWeather>

//    One Call API request
    @GET("/data/2.5/onecall")
    suspend fun weeklyWeather(
        @Query("lat") lat: Float,
        @Query("lon") lon :Float,
        @Query("appid") apikey: String,
        @Query("exclude") exclude: String,
        @Query("units") unit: String
    ): Response<WeeklyForecast>
}