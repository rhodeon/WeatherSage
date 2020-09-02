package com.rhodeon.weathersage.forecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import coil.load
import com.rhodeon.weathersage.*
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.databinding.FragmentCurrentForecastBinding
import java.text.SimpleDateFormat
import java.util.*

class CurrentForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrentForecastViewModel by viewModels()
    private val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.zipcodeEntryButton.setOnClickListener {
            navigateToZipcodeEntry()
        }

        // Check if there is a saved location and display data accordingly
        if (isLocationEmpty(requireContext())) {
            binding.currentEmptyStateText.text = getString(R.string.current_empty_state_text)
        }

        else {
            observeLocation()
            val viewStateObserver = Observer<CurrentWeather> { viewState ->
                binding.currentForecastProgress.isGone = true   // remove progress bar on loaded state
                binding.locationName.text = viewState.locationName
                binding.tempValue.text = formatTempOnDisplay(
                    viewState.forecast.temp,
                    tempDisplaySettingManager.getPreferredUnit()
                )
                binding.dateText.text = DATE_FORMAT.format(Date(viewState.date * 1000))
                val iconId: String = viewState.weather[0].icon
                binding.currentWeatherIcon.load("http://openweathermap.org/img/wn/${iconId}@2x.png")
                binding.currentWeatherIcon.isVisible = true

                binding.currentDescriptionText.text =
                    "Forecast: ${viewState.weather[0].description}"
            }
            viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun  navigateToZipcodeEntry() {
        val action = CurrentForecastFragmentDirections.actionCurrentForecastFragmentToZipcodeEntryFragment2()
        findNavController().navigate(action)
    }

    private fun observeLocation() {
        // Checks for the change in location then updates the weather state
        if (viewModel.viewState.value != null) return
        locationRepository = LocationRepository(requireContext())

//        Create and link an observer for the saved location
        val savedLocationObserver = Observer<Location> {savedLocation ->


            when (savedLocation) {
//                reload forecasts with a change in location
                is Location.Zipcode -> {
                    binding.currentForecastProgress.isVisible = true    // display progress bar while loading state
                    viewModel.loadCurrentForecast(savedLocation.zipcode, getUnitForRequest(requireContext()))    // load weather data
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)
    }
}