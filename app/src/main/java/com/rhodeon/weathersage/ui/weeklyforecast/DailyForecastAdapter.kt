package com.rhodeon.weathersage.ui.weeklyforecast

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rhodeon.weathersage.api.DailyForecast
import com.rhodeon.weathersage.databinding.DailyForecastItemBinding
import com.rhodeon.weathersage.utils.DiffCallbackDelegate
import com.rhodeon.weathersage.utils.UnitDisplayManager
import com.rhodeon.weathersage.utils.formatDate
import com.rhodeon.weathersage.utils.parseIconUrl

class DailyForecastViewHolder(
    private val binding: DailyForecastItemBinding,
    private val unitDisplayManager: UnitDisplayManager
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(dailyForecast: DailyForecast) {
        binding.tempValue.text = unitDisplayManager.formatTemp(dailyForecast.temp.max)
        binding.minTempValue.text = unitDisplayManager.formatTemp(dailyForecast.temp.min)
        binding.tempDescription.text = dailyForecast.weather[0].description
        binding.dailyDate.text = formatDate(dailyForecast.date)

        val iconId: String = dailyForecast.weather[0].icon
        binding.forecastIcon.load(parseIconUrl(iconId))
    }
}

class DailyForecastAdapter(
    private val unitDisplayManager: UnitDisplayManager,
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(
    DIFF_CONFIG
) {
    companion object {
          val DIFF_CONFIG: DiffUtil.ItemCallback<DailyForecast> by DiffCallbackDelegate()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        // For creating a view holder from a view
        val binding =
            DailyForecastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DailyForecastViewHolder(binding, unitDisplayManager)
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        // For displaying data of view holder
        val currentForecast = getItem(position)
        holder.bind(currentForecast)
        holder.itemView.setOnClickListener {
            clickHandler(currentForecast)
        }
    }
}