package com.rhodeon.weathersage

import android.content.Context

enum class TempDisplayUnit {
    CELSIUS, FAHRENHEIT
}

class TempDisplaySettingManager(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun getPreferredUnit(): TempDisplayUnit {
        val preferredUnit = preferences.getString("key_preferred_unit", TempDisplayUnit.CELSIUS.name) ?: TempDisplayUnit.FAHRENHEIT.name
        return TempDisplayUnit.valueOf(preferredUnit)
    }

}