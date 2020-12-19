package com.rhodeon.weathersage.ui.currentforecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rhodeon.weathersage.BuildConfig
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.api.createOpenWeatherMapService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CurrentForecastViewModel : ViewModel() {
    private val _viewState = MutableLiveData<CurrentWeather>()
    val viewState: LiveData<CurrentWeather> = _viewState

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val viewModelJob = Job()
    private val requestScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun loadCurrentForecastByName(cityName: String, countryCode: String, unit: String) {
        val fullLocation = "$cityName,$countryCode"

        requestScope.launch {
            try {
                val response = createOpenWeatherMapService().currentWeatherByName(
                    location = fullLocation,
                    unit = unit,
                    apikey = BuildConfig.OPEN_WEATHER_MAP_API_KEY
                )

                val weatherResponse = response.body()

                if (weatherResponse != null) {
                    _viewState.value = weatherResponse
                } else { // On an error response
                    when (response.code()) {
                        404 -> _message.value = "City not found"
                        else -> _message.value = "Error Loading Current Weather"
                    }
                }

            } catch (t: Exception) {
                Log.e(
                    CurrentForecastViewModel::class.java.simpleName,
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