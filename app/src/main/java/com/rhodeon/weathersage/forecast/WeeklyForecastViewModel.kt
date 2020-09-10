package com.rhodeon.weathersage.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rhodeon.weathersage.BuildConfig
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.api.WeeklyForecast
import com.rhodeon.weathersage.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class WeeklyForecastViewModel : ViewModel() {
    private val _viewState = MutableLiveData<WeeklyForecast>()
    val viewState: LiveData<WeeklyForecast> = _viewState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun loadWeeklyForecastsByName(cityName: String, countryCode: String, unit: String) {
        // Load weeklyForecast with daily forecast data
        // Obtain long and lat from current weather data
        val fullLocation = cityName + "," + countryCode
        val call = createOpenWeatherMapService().currentWeatherByName(fullLocation, unit, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object: Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(WeeklyForecastViewModel::class.java.simpleName, "Error Loading Current Weather", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()

                if (weatherResponse != null) {
                    // Use long and lat as queries to get weekly forecast
                    val call = createOpenWeatherMapService().weeklyWeather(
                        lat = weatherResponse.coord.lat,
                        lon = weatherResponse.coord.lon,
                        apikey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
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

                else {  // On an error response
                    when (response.code()) {
                        404 -> _message.value = "City not found"
                        else -> _message.value = "Error Loading Current Weather"
                    }
                }
            }
        })
    }
}