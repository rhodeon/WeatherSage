package com.rhodeon.weathersage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location {
    data class CountryCode(val city: String, val code: String) : Location()
}

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private  val _savedLocation : MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key != "key_country_code") return@registerOnSharedPreferenceChangeListener
            broadcastSavedLocation()
        }

        broadcastSavedLocation()
    }

    fun saveLocation(location: Location) {
        when(location) {
            is Location.CountryCode -> {
                preferences.edit().putString("key_city_name", location.city).apply()
                preferences.edit().putString("key_country_code", location.code).apply()
            }
        }
    }

    fun saveCountryName(country: String) {
        preferences.edit().putString("key_country_name", country).apply()
    }

    private fun broadcastSavedLocation() {
        val city = preferences.getString("key_city_name", "")
        val code = preferences.getString("key_country_code", "")

        // Update the value of saved location if neither country code or city name if null or blank
        if (!(code.isNullOrBlank())) {
            if (!(city.isNullOrBlank())) {
            _savedLocation.value = Location.CountryCode(city, code)
        }
            }
    }

    fun getSavedLocation(): String {
        val location =  preferences.getString("key_country_code", "")
        return location!!
    }
}