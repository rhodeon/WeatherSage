package com.rhodeon.weathersage

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun formatTempOnDisplay(temp: Float, tempPreferredUnit: TempDisplayUnit): String {
    // Format displayed temperature and unit according to saved preferences
    // Param: temp - value of temperature
    //        tempDisplayUnit - preferred unit stored in settings

    return when(tempPreferredUnit) {
        TempDisplayUnit.CELSIUS -> {
            val tempCelsius = (temp - 32f) * (5f/9f)
            String.format("%.2f °C", tempCelsius)
        }

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

        .setOnDismissListener() {
            Toast.makeText(context, "I will remember this :<", Toast.LENGTH_SHORT).show()
        }

    dialogBuilder.show()
}