package com.creator.myapplication.network

import com.creator.myapplication.model.CurrentWeatherResponse
import com.creator.myapplication.model.ForecastResponse
import com.creator.myapplication.utils.ApiConstants.CURRENT_WEATHER
import com.creator.myapplication.utils.ApiConstants.FORECAST
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiServices {
    @GET(CURRENT_WEATHER)
    suspend fun getCurrentWeather(
        @Query("lat") lats: Double, @Query("lon") longs: Double, @Query("appid") appId: String
    ): Response<CurrentWeatherResponse>

    @GET(FORECAST)
    suspend fun getForecast(
        @Query("lat") lats: Double, @Query("lon") longs: Double, @Query("appid") appId: String
    ): Response<ForecastResponse>
}