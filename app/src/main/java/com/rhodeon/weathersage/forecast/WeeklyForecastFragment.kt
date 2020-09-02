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
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
        observeLocation()

        val dailyForecastAdapter = DailyForecastAdapter(tempDisplaySettingManager) {forecast ->
            navigateToForecastDetails(forecast)
        }
        binding.forecastView.adapter = dailyForecastAdapter

        val viewModelObserver = Observer<WeeklyForecast> { viewState->
            binding.weeklyForecastProgress.isGone = true    // remove progress bar on loaded state
            dailyForecastAdapter.submitList(viewState.daily)
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewModelObserver)

        binding.zipcodeEntryButton.setOnClickListener{
            navigateToZipcodeEntry()
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
                is Location.Zipcode -> {
                    binding.weeklyForecastProgress.isVisible = true     // display progress bar while loading state
                    viewModel.loadWeeklyForecasts(savedLocation.zipcode)    // load weather data
                }
            }
        }
        locationRepository.savedLocation.observe(viewLifecycleOwner, savedLocationObserver)
    }

    private fun navigateToZipcodeEntry() {
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToZipcodeEntryFragment()
        findNavController().navigate(action)
    }

    private fun navigateToForecastDetails(forecast: DailyForecast) {
        val action = WeeklyForecastFragmentDirections.actionWeeklyForecastFragmentToForecastDetailsFragment(forecast.temp.max, forecast.weather[0].description)
        findNavController().navigate(action)
    }
}