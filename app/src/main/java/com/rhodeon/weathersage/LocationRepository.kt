package com.rhodeon.weathersage

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

sealed class Location {
    data class Zipcode(val zipcode: String) : Location()
}

const val KEY_ZIPCODE = "key_zipcode"

class LocationRepository(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    private  val _savedLocation : MutableLiveData<Location> = MutableLiveData()
    val savedLocation: LiveData<Location> = _savedLocation

    init {
        preferences.registerOnSharedPreferenceChangeListener { _, key ->
            if (key != KEY_ZIPCODE) return@registerOnSharedPreferenceChangeListener
            broadcastSavedLocation()
        }

        broadcastSavedLocation()
    }

    fun saveLocation(location: Location) {
        when(location) {
            is Location.Zipcode -> preferences.edit().putString(KEY_ZIPCODE, location.zipcode).apply()
        }
    }

    private fun broadcastSavedLocation() {
        val zipcode = preferences.getString(KEY_ZIPCODE, "")

        if (zipcode != null && zipcode.isNotBlank()) {
            _savedLocation.value = Location.Zipcode(zipcode)
        }
    }

    fun getSavedLocation(): String {
        val location =  preferences.getString(KEY_ZIPCODE, "")
        return location!!
    }
}