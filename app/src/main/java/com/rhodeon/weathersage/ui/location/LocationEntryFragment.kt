package com.rhodeon.weathersage.ui.location

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.rhodeon.weathersage.Location
import com.rhodeon.weathersage.LocationRepository
import com.rhodeon.weathersage.R
import com.rhodeon.weathersage.database.AppDatabase
import com.rhodeon.weathersage.databinding.FragmentLocationEntryBinding
import com.rhodeon.weathersage.utils.navigateSafe

class LocationEntryFragment : Fragment() {
    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val db = Room.databaseBuilder(requireContext(), AppDatabase::class.java, "countries.db")
            .createFromAsset("database/countries.db")
            .allowMainThreadQueries()
            .build()

        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)
        val locationRepository = LocationRepository(requireContext())
        val preferences = requireContext().getSharedPreferences("settings", Context.MODE_PRIVATE)

        val countryList = db.countryDao().getCountryList()      // array to hold country names for validation

        val autofillAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, countryList)
        binding.countryInput.setAdapter(autofillAdapter)

        binding.countryText.text = preferences.getString("key_country_name", "Country Name")

        binding.editCountryIcon.setOnClickListener {editIcon ->
            // Make only country input visible and editable
            binding.countryInput.isVisible = true
            binding.countryText.isInvisible = true  // hide country name
            editIcon.isInvisible = true
            binding.countryInput.requestFocus()     // switch focus to country input
        }

        binding.countryInput.setOnFocusChangeListener { countryInput: View, hasFocus: Boolean ->
            if (hasFocus) {
                // Display the keyboard
                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(countryInput, InputMethodManager.SHOW_IMPLICIT)
            }
            else {
                // If user is done typing, display the country name and pencil icon
                binding.countryInput.isInvisible = true     // hide edit text box
                binding.countryText.isVisible = true        // display country name
                binding.countryText.text = binding.countryInput.text
                binding.editCountryIcon.isVisible = true

                if (!countryList.contains(binding.countryInput.text.toString())) {
                    // If country isn't valid
                    Toast.makeText(requireContext(), "Invalid country", Toast.LENGTH_SHORT).show()
                    binding.countryText.text = getString(R.string.default_country_name)
                }
            }
        }

        binding.countryInput.setOnDismissListener {
            // When an option is selected from the autofill list
            binding.countryText.isVisible = true
            binding.editCountryIcon.isVisible = true
            binding.countryInput.isInvisible = true
            binding.countryText.text = binding.countryInput.text
        }


        binding.submitButton.setOnClickListener {
            /*
                Save location information and switch to the current weather fragment
             */

            if (!countryList.contains(binding.countryText.text.toString())) {   // if no valid country is entered, halt
                Toast.makeText(requireContext(), "Enter a Valid Country", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.cityInput.text.toString().isBlank()) {  // if no city is entered, halt
                Toast.makeText(requireContext(), "Enter a City", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val countryName = binding.countryText.text.toString()
            val cityName = binding.cityInput.text.toString()
            val countryCode = db.countryDao().getCountryCode(countryName)     // get corresponding country code from the database

            // Save country name, code and city name to shared preferences for future use
            locationRepository.saveLocation(Location.CountryCode(cityName, countryCode))
            locationRepository.saveCountryName(countryName)

            navigateToCurrentForecast()
        }

        return  binding.root
    }

    private fun navigateToCurrentForecast() {
        val action =
            LocationEntryFragmentDirections.actionLocationEntryFragmentToCurrentForecastFragment()
        navigateSafe(action)
    }
}