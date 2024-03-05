package com.creator.myapplication.network

sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null,
) {
    class Loading<T>(data: T? = null) : ApiResponse<T>(data = data)
    class Success<T>(data: T) : ApiResponse<T>(data = data)
    class Error<T>(data: T? = null, message: String) : ApiResponse<T>(data, message)
}