package com.rhodeon.weathersage.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import com.rhodeon.weathersage.LocationRepository
import java.text.DateFormat.getDateInstance
import java.util.*

fun isLocationEmpty(context: Context): Boolean {
    // Checks if a saved location exists
    val locationRepository = LocationRepository(context)
    val location = locationRepository.getSavedLocation()
    if (location.isBlank()) {
        return true
    }
    return false
}

fun getUnitForRequest(context: Context): String {
    // Returns temperature unit to be used for API query
    val unitDisplayManager = UnitDisplayManager(context)
    val savedUnit = unitDisplayManager.preferredUnit

    return if (savedUnit == Unit.METRIC) {
        "metric"
    }
    else "imperial"
}

fun parseIconUrl(iconId: String): String {
    // Returns the url of the weather icon
    return "https://openweathermap.org/img/wn/${iconId}@2x.png"
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val capabilities =
        connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
    if (capabilities != null) {
        if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
            return true
        } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
            Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
            return true
        }
    }
    return false
}

fun formatDate(date: Long): String {
    return getDateInstance().format(Date(date * 1000))
}