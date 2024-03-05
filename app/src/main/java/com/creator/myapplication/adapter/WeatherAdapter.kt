package com.creator.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.creator.myapplication.R
import com.creator.myapplication.databinding.ItemWeatherInfoBinding
import com.creator.myapplication.model.ForecastResponse

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {
    private val weatherList: ArrayList<ForecastResponse.WeatherItem> = ArrayList()
    var nameOfCity: String = ""

    inner class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_weather_info, parent, false))
    }

    override fun getItemCount() = weatherList.size

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        with(holder) {
            val binding = DataBindingUtil.bind<ItemWeatherInfoBinding>(itemView)
            if (binding != null) {
                binding.cityName = nameOfCity
                binding.weatherInfo = weatherList[position]
            }
        }
    }

    fun setList(newList: ArrayList<ForecastResponse.WeatherItem>) {
        weatherList.addAll(newList)
        notifyDataSetChanged()
    }
}