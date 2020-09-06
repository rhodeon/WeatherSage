package com.rhodeon.weathersage.forecast

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhodeon.weathersage.*
import com.rhodeon.weathersage.api.DailyForecast
import com.rhodeon.weathersage.api.WeeklyForecast
import com.rhodeon.weathersage.databinding.FragmentWeeklyForecastBinding

class WeeklyForecastFragment : Fragment() {
    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private lateinit var locationRepository: LocationRepository
    private var _binding: FragmentWeeklyForecastBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeeklyForecastViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentWeeklyForecastBinding.inflate(inflater, container, false)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        binding.forecastView.layoutManager = LinearLayoutManager(requireContext())


        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateToLocationEntryFab.setOnClickListener{
            navigateToLocationEntry()
        }

        // Check if there is a saved location and display data accordingly
        if (isLocationEmpty(requireContext())) {
            binding.weeklyEmptyStateText.text = getString(R.string.weekly_empty_state_text)
        } else {
            observeLocation()

            val dailyForecastAdapter =
                DailyForecastAdapter(tempDisplaySettingManager) { forecast ->
                    navigateToForecastDetails(forecast)
                }
            binding.forecastView.adapter = dailyForecastAdapter

            // Add horizontal divider to recycler view
            binding.forecastView.addItemDecoration(
                DividerItemDecoration(
                    requireContext(),
                    LinearLayoutManager.VERTICAL
                )
            )

            val viewModelObserver = Observer<WeeklyForecast> { viewState ->
                binding.weeklyForecastProgress.isGone =
                    true    // remove progress bar on loaded state
                dailyForecastAdapter.submitList(viewState.daily)    // update list adapter
            }
            viewModel.viewState.observe(viewLifecycleOwner, viewModelObserver)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeLocation() {
        // Checks for the change in location then updates the weather state
        if (viewModel.viewState.value != null) return

        locationRepository = LocationRepository(requireContext())
        val savedLocationObserver = Observer<Location> {savedLocation ->
            when (savedLocation) {
                is Location.CountryCode -> {
                    binding.weeklyForecastProgress.isVisible = true     // display progress bar while loading state
                    if (isOnline(requireContext())) {
                        viewModel.loadWeeklyForecastsByName(savedLocation.city, savedLocation.code, getUnitForRequest(requireContext()))    // load weather data
                    }
                    else {
                        binding.weeklyForecastProgress.isGone = true
                        Toast.makeText(requireContext(), "Error loading weekly forecast\nCheck internet connection", Toast.LENGTH_SHORT).show()
                        return@Observer
                    }

                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)
    }

    private fun navigateToLocationEntry() {
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToLocationEntryFragment()
        findNavController().navigate(action)
    }

    private fun navigateToForecastDetails(forecast: DailyForecast) {
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(
            maxTemp = forecast.temp.max,
            minTemp = forecast.temp.min ,
            description = forecast.weather[0].description,
            iconId = forecast.weather[0].icon
            )

        findNavController().navigate(action)
    }
}