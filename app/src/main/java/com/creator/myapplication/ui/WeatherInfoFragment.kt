package com.creator.myapplication.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.creator.myapplication.R
import com.creator.myapplication.adapter.WeatherAdapter
import com.creator.myapplication.base.BaseFragment
import com.creator.myapplication.databinding.FragmentWeatherInfoBinding
import com.creator.myapplication.model.LatLong
import com.creator.myapplication.network.ApiResponse
import com.creator.myapplication.utils.ApiConstants.APP_ID
import com.creator.myapplication.viewmodel.MainViewModel
import com.google.android.material.tabs.TabLayout

class WeatherInfoFragment : BaseFragment() {

    private lateinit var binding: FragmentWeatherInfoBinding
    private val loginViewModel: MainViewModel by viewModels()
    private val weatherAdapter: WeatherAdapter by lazy {
        WeatherAdapter()
    }
    private lateinit var latLong: LatLong
    private val locationArgs: WeatherInfoFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setView(R.layout.fragment_weather_info)
    }

    override fun initView() {
        latLong = locationArgs.latLong
        binding = getBinding()
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            getWeatherInformationFromApi()

            rvWeather.adapter = weatherAdapter

            weatherTabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.text) {
                        getString(R.string.str_current_weather) -> {
                            getWeatherInformationFromApi()
                            rvWeather.visibility = View.GONE
                            cardWeather.visibility = View.VISIBLE
                        }

                        getString(R.string.str_5_days_forecast) -> {
                            getForecastInformation()
                            rvWeather.visibility = View.VISIBLE
                            cardWeather.visibility = View.GONE
                        }
                    }
                }

                override fun onTabUnselected(p0: TabLayout.Tab?) {
                }

                override fun onTabReselected(p0: TabLayout.Tab?) {
                }
            })
        }
    }

    private fun getWeatherInformationFromApi() {
        loginViewModel.getCurrentWeather(
            lats = latLong.lat, longs = latLong.long, appId = APP_ID
        ).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    hideProgressDialog()
                    if (it.data != null) {
                        binding.apply {
                            weatherInfo = it.data
                        }
                    }
                }

                is ApiResponse.Error -> {
                    Log.e("TAG", "getWeatherInformationFromApi: ${it.message.toString()}")
                    hideProgressDialog()
                }

                is ApiResponse.Loading -> {
                    showProgressDialog()
                }

                else -> {
                    hideProgressDialog()
                }
            }
        }
    }

    private fun getForecastInformation() {
        loginViewModel.getForecast(
            lats = latLong.lat, longs = latLong.long, appId = APP_ID
        ).observe(viewLifecycleOwner) {
            when (it) {
                is ApiResponse.Success -> {
                    hideProgressDialog()
                    if (it.data != null) {
                        with(it.data) {
                            weatherAdapter.nameOfCity = city?.name.toString()
                            weatherAdapter.setList(list)
                        }
                    }
                }

                is ApiResponse.Error -> {
                    Log.e("TAG", "getWeatherInformationFromApi: ${it.message.toString()}")
                    hideProgressDialog()
                }

                is ApiResponse.Loading -> {
                    showProgressDialog()
                }

                else -> {
                    hideProgressDialog()
                }
            }
        }
    }
}