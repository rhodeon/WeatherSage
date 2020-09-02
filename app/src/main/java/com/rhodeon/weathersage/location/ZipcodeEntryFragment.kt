package com.rhodeon.weathersage.location

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.rhodeon.weathersage.Location
import com.rhodeon.weathersage.LocationRepository
import com.rhodeon.weathersage.R

class ZipcodeEntryFragment : Fragment() {
    private lateinit var locationRepository: LocationRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        locationRepository = LocationRepository(requireContext())

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_zipcode_entry, container, false)

        val inputZipcode: EditText = view.findViewById(R.id.input_zipcode)
        val submitZipcodeButton: Button = view.findViewById(R.id.submit_button)

        submitZipcodeButton.setOnClickListener {
            val zipcode: String = inputZipcode.text.toString()
            if (zipcode.length == 5) {
                locationRepository.saveLocation(Location.Zipcode(zipcode))
                navigateToCurrentForecast()
            }
            else {
                Toast.makeText(requireContext(), R.string.invalid_zipcode_length, Toast.LENGTH_SHORT).show()
            }

        }

        return view
    }

    private fun navigateToCurrentForecast() {
        val action = ZipcodeEntryFragmentDirections.actionZipcodeEntryFragmentToCurrentForecastFragment4()
        findNavController().navigate(action)
    }
}