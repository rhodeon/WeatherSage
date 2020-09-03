package com.rhodeon.weathersage.forecast

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rhodeon.weathersage.BuildConfig
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.api.createOpenWeatherMapService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CurrentForecastViewModel : ViewModel() {
    private val _viewState = MutableLiveData<CurrentWeather>()
    val viewState: LiveData<CurrentWeather> = _viewState

    fun loadCurrentForecast(zipcode: String, unit: String) {
        val call = createOpenWeatherMapService().currentWeather(zipcode, unit, BuildConfig.OPEN_WEATHER_MAP_API_KEY)
        call.enqueue(object: Callback<CurrentWeather> {
            override fun onFailure(call: Call<CurrentWeather>, t: Throwable) {
                Log.e(CurrentForecastViewModel::class.java.simpleName, "Error Loading Current Weather", t)
            }

            override fun onResponse(call: Call<CurrentWeather>, response: Response<CurrentWeather>) {
                val weatherResponse = response.body()

                if (weatherResponse != null) {
                    _viewState.value = weatherResponse
                }
            }

        })
    }

}