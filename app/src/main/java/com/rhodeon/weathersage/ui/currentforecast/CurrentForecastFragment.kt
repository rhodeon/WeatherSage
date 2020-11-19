package com.rhodeon.weathersage.ui.currentforecast

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import coil.load
import com.rhodeon.weathersage.*
import com.rhodeon.weathersage.api.CurrentWeather
import com.rhodeon.weathersage.databinding.FragmentCurrentForecastBinding
import com.rhodeon.weathersage.utils.*

class CurrentForecastFragment : Fragment() {
    private lateinit var unitDisplayManager: UnitDisplayManager
    private lateinit var locationRepository: LocationRepository
    private var _binding: FragmentCurrentForecastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CurrentForecastViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrentForecastBinding.inflate(inflater, container, false)
        unitDisplayManager = UnitDisplayManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateToLocationEntryFab.setOnClickListener {
            navigateToLocationEntry()
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
                binding.tempValue.text = unitDisplayManager.formatTemp(viewState.forecast.temp)
                binding.date.text = formatDate(viewState.date)

                val iconId: String = viewState.weather[0].icon
                binding.currentWeatherIcon.load(
                    parseIconUrl(
                        iconId
                    )
                )
                binding.currentWeatherIcon.isVisible = true
                binding.currentDescriptionText.text = viewState.weather[0].description
                binding.countryCodeText.text = viewState.sys.country

                val miscDetailsView = binding.miscDetailsView
                miscDetailsView.layout.isVisible = true
                miscDetailsView.humidityValue.text = unitDisplayManager.formatHumidity(viewState.forecast.humidity)
                miscDetailsView.pressureValue.text = unitDisplayManager.formatPressure(viewState.forecast.pressure)
                miscDetailsView.windSpeedValue.text = unitDisplayManager.formatWindSpeed(viewState.wind.speed)
            }
            viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
        }

        val responseMessageObserver = Observer<String> {message ->
            if (!message.isNullOrBlank()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                binding.currentForecastProgress.isGone = true   // remove progress bar on loaded state
            }
        }
        viewModel.message.observe(viewLifecycleOwner, responseMessageObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun  navigateToLocationEntry() {
        val action =
            CurrentForecastFragmentDirections.actionCurrentForecastFragmentToLocationEntryFragment()
        navigateSafe(action)
    }

    private fun observeLocation() {
        // Checks for the change in location then updates the weather state
        if (viewModel.viewState.value != null) return
        locationRepository = LocationRepository(requireContext())

//        Create and link an observer for the saved location
        val savedLocationObserver = Observer<Location> {savedLocation ->

            when (savedLocation) {
//                reload forecasts with a change in location
                is Location.CountryCode -> {
                    binding.currentForecastProgress.isVisible = true    // display progress bar while loading state
                    if (isOnline(requireContext())) {
                        viewModel.loadCurrentForecastByName(savedLocation.city, savedLocation.code,
                            getUnitForRequest(
                                requireContext()
                            )
                        )    // load weather data
                    }
                    else {
                        binding.currentForecastProgress.isGone = true
                        Toast.makeText(requireContext(), "Error loading current weather\n" + "Check internet connection", Toast.LENGTH_SHORT).show()
                        return@Observer
                    }
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)
    }
}