package com.rhodeon.weathersage.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.api.WeeklyForecast
import com.rhodeon.weathersage.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeeklyForecastViewModel : ViewModel() {
    private val _viewState = MutableLiveData<WeeklyForecast>()
    val viewState: LiveData<WeeklyForecast> = _viewState

    fun loadWeeklyForecasts(zipcode: String, unit: String) {
        // Load weeklyForecast with daily forecast data
        // Obtain long and lat from current weather data
        val call = createOpenWeatherMapService().currentWeather(zipcode, unit, "fd034db52f3033a3f6dcc38f1b16420d")
        call.enqueue(object: Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(WeeklyForecastViewModel::class.java.simpleName, "Error Loading Current Weather", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()

                if (weatherResponse != null) {
                    val call = createOpenWeatherMapService().weeklyWeather(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        apikey = "fd034db52f3033a3f6dcc38f1b16420d",
                        exclude = "current,minutely,hourly",
                        unit = unit

                    )

                    call.enqueue(object: Callback<WeeklyForecast> {
                        override fun onFailure(call: Call<WeeklyForecast>, t: Throwable) {
                            Log.e(WeeklyForecastViewModel::class.java.simpleName, "Error loading weekly forecast", t)
                        }

                        override fun onResponse(
                            call: Call<WeeklyForecast>, response: Response<WeeklyForecast>
                        ) {
                            val weeklyWeatherResponse = response.body()

                            if (weeklyWeatherResponse != null) {
                                _viewState.value = weeklyWeatherResponse
                            }
                        }

                    })
                }
            }

        })

    }

}