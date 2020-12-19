package com.rhodeon.weathersage.ui.weeklyforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rhodeon.weathersage.BuildConfig
import com.rhodeon.weathersage.api.WeeklyForecast
import com.rhodeon.weathersage.api.createOpenWeatherMapService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class WeeklyForecastViewModel : ViewModel() {
    private val _viewState = MutableLiveData<WeeklyForecast>()
    val viewState: LiveData<WeeklyForecast> = _viewState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val viewModelJob = Job()
    val requestScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun loadWeeklyForecastsByName(cityName: String, countryCode: String, unit: String) {

        // Load weeklyForecast with daily forecast data
        // Obtain long and lat from current weather data
        val fullLocation = "$cityName,$countryCode"

        requestScope.launch {
            try {
                val response = createOpenWeatherMapService().currentWeatherByName(
                    fullLocation,
                    unit,
                    BuildConfig.OPEN_WEATHER_MAP_API_KEY
                )

                val currentResponse = response.body()
                if (currentResponse != null) {
                    // Use long and lat as queries to get weekly forecast
                    try {
                        val call = createOpenWeatherMapService().weeklyWeather(
                            lat = currentResponse.coord.lat,
                            lon = currentResponse.coord.lon,
                            apikey = BuildConfig.OPEN_WEATHER_MAP_API_KEY,
                            exclude = "current,minutely,hourly",
                            unit = unit
                        )

                        val weeklyWeatherResponse = call.body()

                        if (weeklyWeatherResponse != null) {
                            _viewState.value = weeklyWeatherResponse
                        }

                    } catch (t: Throwable) {
                        Log.e(
                            WeeklyForecastViewModel::class.java.simpleName,
                            "Error Loading Weekly Forecast: ${t.message}"
                        )

                    }

                } else {  // On an error response
                    when (response.code()) {
                        404 -> _message.value = "City not found"
                        else -> _message.value = "Error Loading Current Weather"
                    }
                }

            } catch (t: Throwable) {
                Log.e(
                    WeeklyForecastViewModel::class.java.simpleName,
                    "Error Loading Current Weather: ${t.message}"
                )
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.complete()
    }
}