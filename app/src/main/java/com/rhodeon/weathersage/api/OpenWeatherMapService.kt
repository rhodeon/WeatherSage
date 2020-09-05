package com.rhodeon.weathersage.api

import retrofit2.Call
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
    @GET("/data/2.5/weather")
    fun currentWeather(
        @Query("zip") zipcode: String,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<CurrentWeather>

//    One Call API request
    @GET("/data/2.5/onecall")
    fun weeklyWeather(
        @Query("lat") lat: Float,
        @Query("lon") lon :Float,
        @Query("appid") apikey: String,
        @Query("exclude") exclude: String,
        @Query("units") unit: String
    ): Call<WeeklyForecast>

    @GET("/data/2.5/weather")
    fun currentWeatherByLocation (
        @Query("q") location: String,
        @Query("units") units: String,
        @Query("appid") apikey: String
    ): Call<CurrentWeather>
}