package com.rhodeon.weathersage.utils

import android.content.Context

/**
 * Created by Ogheneruona Onobrakpeya on 11/19/20.
 */

enum class Unit {
    METRIC, IMPERIAL
}

class UnitDisplayManager(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
    private val unit = preferences.getString("key_preferred_unit", Unit.METRIC.name) ?: Unit.METRIC.name
    val preferredUnit = Unit.valueOf(unit)

    fun formatTemp(temp: Float): String {
        // Format displayed temperature and unit according to saved preferences
        // Param: temp - value of temperature
        //        tempDisplayUnit - preferred unit stored in settings

        return when(preferredUnit) {
            Unit.METRIC -> String.format("%.2f °C", temp)
            Unit.IMPERIAL ->  String.format("%.2f °F", temp)
        }
    }

    fun formatHumidity(humidity: Float): String {
        return String.format("%.2f %%", humidity)
    }

    fun formatPressure(pressure: Float): String {
        return String.format("%.2f hPa", pressure)
    }

    fun formatWindSpeed(windSpeed: Float): String  {
        return when(preferredUnit) {
            Unit.METRIC -> String.format("%.2f m/s", windSpeed)
            Unit.IMPERIAL -> String.format("%.2f mph", windSpeed)
        }
    }
}