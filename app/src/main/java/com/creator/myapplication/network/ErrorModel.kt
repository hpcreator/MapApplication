package com.creator.myapplication.network

import com.google.gson.annotations.SerializedName

data class ErrorModel(
    @SerializedName("status") val status: Int?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Any? = null
)