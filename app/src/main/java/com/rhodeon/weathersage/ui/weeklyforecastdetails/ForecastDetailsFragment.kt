package com.rhodeon.weathersage.ui.weeklyforecastdetails

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rhodeon.weathersage.utils.TempDisplaySettingManager
import com.rhodeon.weathersage.databinding.FragmentForecastDetailsBinding
import com.rhodeon.weathersage.utils.formatDate
import com.rhodeon.weathersage.utils.formatTempOnDisplay
import com.rhodeon.weathersage.utils.parseIconUrl

class ForecastDetailsFragment : BottomSheetDialogFragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = {viewModelFactory}
    )
    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager =
            TempDisplaySettingManager(
                requireContext()
            )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> {viewState ->
            val forecast = viewState.forecast

            // Update the UI
            val tempView = binding.temperatureView
            tempView.tempCard.isClickable = false

            val maxTemp: String =
                formatTempOnDisplay(
                    forecast.temp.max,
                    tempDisplaySettingManager.getPreferredUnit()
                )
            tempView.tempValue.append(maxTemp)

            val minTemp = formatTempOnDisplay(
                forecast.temp.min,
                tempDisplaySettingManager.getPreferredUnit()
            )
            tempView.minTempValue.append(minTemp)

            tempView.tempDescription.append(forecast.weather[0].description)

            val iconId = forecast.weather[0].icon
            tempView.forecastIcon.load(parseIconUrl(iconId))
            tempView.forecastIcon.isVisible = true

            tempView.dailyDate.text = formatDate(viewState.forecast.date)

        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}