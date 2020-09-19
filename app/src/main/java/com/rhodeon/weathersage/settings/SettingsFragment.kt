package com.rhodeon.weathersage.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.rhodeon.weathersage.R

/**
 * Created by Ogheneruona Onobrakpeya on 9/19/20.
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = "settings"
        setPreferencesFromResource(R.xml.fragment_settings, rootKey)
    }
}