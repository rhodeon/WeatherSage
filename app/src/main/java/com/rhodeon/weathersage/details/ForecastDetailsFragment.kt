package com.rhodeon.weathersage.details

import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import coil.load
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rhodeon.weathersage.TempDisplaySettingManager
import com.rhodeon.weathersage.databinding.FragmentForecastDetailsBinding
import com.rhodeon.weathersage.formatTempOnDisplay
import com.rhodeon.weathersage.parseIconUrl

class ForecastDetailsFragment : BottomSheetDialogFragment() {
    private val args: ForecastDetailsFragmentArgs by navArgs()
    private val viewModel: ForecastDetailsViewModel by viewModels(
        factoryProducer = {viewModelFactory}
    )
    private lateinit var viewModelFactory: ForecastDetailsViewModelFactory

    private lateinit var tempDisplaySettingManager: TempDisplaySettingManager
    private var _binding: FragmentForecastDetailsBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForecastDetailsBinding.inflate(inflater, container, false)
        viewModelFactory = ForecastDetailsViewModelFactory(args)
        tempDisplaySettingManager = TempDisplaySettingManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewStateObserver = Observer<ForecastDetailsViewState> {viewState ->
            // Update the UI
            val maxTemp: String = formatTempOnDisplay(viewState.maxTemp, tempDisplaySettingManager.getPreferredUnit())
            binding.maxTempDetails.append(maxTemp)

            val minTemp = formatTempOnDisplay(viewState.minTemp, tempDisplaySettingManager.getPreferredUnit())
            binding.minTempDetails.append(minTemp)

            binding.tempDetailsDescription.append(viewState.tempDescription)

            val iconId = viewState.iconId
            binding.detailsIcon.load(parseIconUrl(iconId))
            binding.detailsIcon.isVisible = true
        }
        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}