package com.rhodeon.weathersage.ui.weeklyforecastdetails

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rhodeon.weathersage.databinding.FragmentForecastDetailsBinding
import com.rhodeon.weathersage.utils.*

class ForecastDetailsFragment : BottomSheetDialogFragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = {viewModelFactory}
    )
    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory

    private lateinit var unitDisplayManager: UnitDisplayManager
    private var _binding: FragmentForecastDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        unitDisplayManager = UnitDisplayManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> {viewState ->
            val forecast = viewState.forecast

            // Update the UI
            val tempView = binding.temperatureView
            tempView.tempCard.isClickable = false

            val maxTemp: String = unitDisplayManager.formatTemp(forecast.temp.max)
            tempView.tempValue.append(maxTemp)

            val minTemp = unitDisplayManager.formatTemp(forecast.temp.min)
            tempView.minTempValue.append(minTemp)

            tempView.tempDescription.append(forecast.weather[0].description)

            val iconId = forecast.weather[0].icon
            tempView.forecastIcon.load(parseIconUrl(iconId))
            tempView.forecastIcon.isVisible = true

            tempView.dailyDate.text = formatDate(viewState.forecast.date)

            val miscDetailsView = binding.miscDetailsView
            miscDetailsView.layout.isVisible = true
            miscDetailsView.humidityValue.text = unitDisplayManager.formatHumidity(forecast.humidity)
            miscDetailsView.pressureValue.text = unitDisplayManager.formatPressure(forecast.pressure)
            miscDetailsView.windSpeedValue.text = unitDisplayManager.formatWindSpeed(forecast.windSpeed)

        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}