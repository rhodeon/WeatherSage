package com.rhodeon.weathersage.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.room.Room
import com.rhodeon.weathersage.Location
import com.rhodeon.weathersage.LocationRepository
import com.rhodeon.weathersage.database.AppDatabase
import com.rhodeon.weathersage.databinding.FragmentLocationEntryBinding

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

        val locationRepository = LocationRepository(requireContext())
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)

        binding.submitButton.setOnClickListener {
            val countryName = binding.countryInput.text.toString()
            val countryCode = db.countryDao().getCountryCode(countryName) ?: "Null"
            val cityName = binding.cityInput.text.toString()
            locationRepository.saveLocation(Location.CountryCode(cityName, countryCode))

            navigateToCurrentForecast()
        }

        return  binding.root
    }

    private fun navigateToCurrentForecast() {
        val action = LocationEntryFragmentDirections.actionLocationEntryFragmentToCurrentForecastFragment()
        findNavController().navigate(action)
    }
}