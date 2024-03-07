package com.creator.myapplication.repository

import com.creator.myapplication.model.CurrentWeatherResponse
import com.creator.myapplication.model.ForecastResponse
import com.creator.myapplication.network.ApiServices
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiService: ApiServices) {
    suspend fun getCurrentWeather(
        lats: Double,
        longs: Double,
        appId: String
    ): Response<CurrentWeatherResponse> {
        return apiService.getCurrentWeather(lats, longs, appId)
    }

    suspend fun getForecast(
        lats: Double,
        longs: Double,
        appId: String
    ): Response<ForecastResponse> {
        return apiService.getForecast(lats, longs, appId)
    }
}