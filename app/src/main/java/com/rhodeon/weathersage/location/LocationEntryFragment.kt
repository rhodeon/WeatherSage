package com.rhodeon.weathersage.location

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rhodeon.weathersage.Location
import com.rhodeon.weathersage.LocationRepository
import com.rhodeon.weathersage.countryCodeMap
import com.rhodeon.weathersage.databinding.FragmentLocationEntryBinding

class LocationEntryFragment : Fragment() {
    private var _binding: FragmentLocationEntryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val locationRepository = LocationRepository(requireContext())
        _binding = FragmentLocationEntryBinding.inflate(inflater, container, false)

        binding.submitButton.setOnClickListener {
            val countryName = binding.countryInput.text.toString()
            val countryCode = countryCodeMap[countryName]
            val cityName = binding.cityInput.text.toString()
            locationRepository.saveLocation(Location.CountryCode(cityName, countryName))

            navigateToCurrentForecast()
        }

        return  binding.root
    }

    private fun navigateToCurrentForecast() {
        val action = LocationEntryFragmentDirections.actionLocationEntryFragmentToCurrentForecastFragment()
        findNavController().navigate(action)
    }
}