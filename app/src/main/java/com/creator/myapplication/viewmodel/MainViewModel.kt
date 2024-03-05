package com.creator.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.creator.myapplication.network.ApiResponse
import com.creator.myapplication.network.ErrorModel
import com.creator.myapplication.repository.MainRepository
import com.creator.myapplication.utils.ApiConstants.STATUS_OK
import com.creator.myapplication.utils.NetworkExtensions
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository) : ViewModel() {

    fun getCurrentWeather(lats: Double, longs: Double, appId: String) = liveData(Dispatchers.IO) {
        emit(ApiResponse.Loading(null))
        try {
            val apiResponse = mainRepository.getCurrentWeather(lats, longs, appId)
            when {
                apiResponse.code() == STATUS_OK -> {
                    emit(ApiResponse.Success(apiResponse.body()))
                }

                else -> {
                    val errorModel: ErrorModel? = NetworkExtensions.getErrorMessage(apiResponse.errorBody())
                    emit(ApiResponse.Error(null, errorModel?.message.toString()))
                }
            }
        } catch (exception: Exception) {
            emit(exception.localizedMessage?.let { ApiResponse.Error(null, it) })
        }
    }

    fun getForecast(lats: Double, longs: Double, appId: String) = liveData(Dispatchers.IO) {
        emit(ApiResponse.Loading(null))
        try {
            val apiResponse = mainRepository.getForecast(lats, longs, appId)
            when {
                apiResponse.code() == STATUS_OK -> {
                    emit(ApiResponse.Success(apiResponse.body()))
                }

                else -> {
                    val errorModel: ErrorModel? = NetworkExtensions.getErrorMessage(apiResponse.errorBody())
                    emit(ApiResponse.Error(null, errorModel?.message.toString()))
                }
            }
        } catch (exception: Exception) {
            emit(exception.localizedMessage?.let { ApiResponse.Error(null, it) })
        }
    }
}