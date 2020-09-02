package com.rhodeon.weathersage

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTempOnDisplay(temp: Float, tempPreferredUnit: TempDisplayUnit): String {
    // Format displayed temperature and unit according to saved preferences
    // Param: temp - value of temperature
    //        tempDisplayUnit - preferred unit stored in settings

    return when(tempPreferredUnit) {
        TempDisplayUnit.CELSIUS -> String.format("%.2f °C", temp)
        TempDisplayUnit.FAHRENHEIT ->  String.format("%.2f °F", temp)
    }
}

fun showChangeUnitDialog(context: Context, tempDisplaySettingManager: TempDisplaySettingManager) {
    // Create an alert dialog for changing displayed unit

    val dialogBuilder = AlertDialog.Builder(context)
        .setTitle(R.string.change_unit_dialog_title)
        .setPositiveButton("Fahrenheit (°F)") {_, _ ->
            tempDisplaySettingManager.updateSettings(TempDisplayUnit.FAHRENHEIT)
        }

        .setNeutralButton("Celsius (°C)") {_, _ ->
            tempDisplaySettingManager.updateSettings(TempDisplayUnit.CELSIUS)
        }

//        .setOnDismissListener {
//            Toast.makeText(context, "I will remember this :<", Toast.LENGTH_SHORT).show()
//        }

    dialogBuilder.show()
}

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
    val tempDisplaySettingManager = TempDisplaySettingManager(context)
    val savedUnit = tempDisplaySettingManager.getPreferredUnit()

    return if (savedUnit == TempDisplayUnit.CELSIUS) {
        "metric"
    }
    else "imperial"     // FAHRENHEIT
}

fun isOnline(context: Context): Boolean {
    val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (connectivityManager != null) {
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
    }
    return false
}