package com.rhodeon.weathersage

import android.content.Context

enum class TempDisplayUnit {
    CELSIUS, FAHRENHEIT
}

class TempDisplaySettingManager(context: Context) {
    private val preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE)

    fun updateSettings(preferredUnit: TempDisplayUnit) {
        preferences.edit().putString("key_preferred_unit", preferredUnit.name).commit()
    }

    fun getPreferredUnit(): TempDisplayUnit {
        val preferredUnit = preferences.getString("key_preferred_unit", TempDisplayUnit.CELSIUS.name) ?: TempDisplayUnit.FAHRENHEIT.name
        return TempDisplayUnit.valueOf(preferredUnit)
    }

}