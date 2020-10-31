package com.rhodeon.weathersage.ui.weeklyforecast

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.rhodeon.weathersage.R
import com.rhodeon.weathersage.utils.TempDisplaySettingManager
import com.rhodeon.weathersage.api.DailyForecast
import com.rhodeon.weathersage.utils.formatTempOnDisplay
import com.rhodeon.weathersage.utils.parseIconUrl
import java.text.SimpleDateFormat
import java.util.*

private val DATE_FORMAT = SimpleDateFormat("dd-MM-yyyy")

class DailyForecastViewHolder(
    view: View,
    private val tempDisplaySettingManager: TempDisplaySettingManager
) : RecyclerView.ViewHolder(view) {
    private val tempValue: TextView = view.findViewById(R.id.temp_value)
    private val minTemp: TextView = view.findViewById(R.id.min_temp_value)
    private val tempDescription: TextView = view.findViewById(R.id.temp_description)
    private val dailyDate: TextView = view.findViewById(R.id.daily_date)
    private val forecastIcon: ImageView = view.findViewById(R.id.forecast_icon)

    fun display(dailyForecast: DailyForecast) {
        tempValue.text = formatTempOnDisplay(
            dailyForecast.temp.max,
            tempDisplaySettingManager.getPreferredUnit()
        )
        minTemp.text = formatTempOnDisplay(
            dailyForecast.temp.min,
            tempDisplaySettingManager.getPreferredUnit()
        )
        tempDescription.text = dailyForecast.weather[0].description
        dailyDate.text = DATE_FORMAT.format(Date(dailyForecast.date * 1000))

        val iconId: String = dailyForecast.weather[0].icon
        forecastIcon.load(parseIconUrl(iconId))
    }
}

class DailyForecastAdapter (
    private val tempDisplaySettingManager: TempDisplaySettingManager,
    private val clickHandler: (DailyForecast) -> Unit
) : ListAdapter<DailyForecast, DailyForecastViewHolder>(
    DIFF_CONFIG
) {

    companion object {
        val DIFF_CONFIG = object: DiffUtil.ItemCallback<DailyForecast>() {
            override fun areItemsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: DailyForecast, newItem: DailyForecast): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DailyForecastViewHolder {
        // For creating a view holder from a view
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.daily_forecast_item, parent, false)
        return DailyForecastViewHolder(
            itemView,
            tempDisplaySettingManager
        )
    }

    override fun onBindViewHolder(holder: DailyForecastViewHolder, position: Int) {
        // For displaying data of view holder
        val currentForecast = getItem(position)
        holder.display(currentForecast)
        holder.itemView.setOnClickListener{
            clickHandler(currentForecast)
        }
    }
}