package com.creator.myapplication.utils

import com.creator.myapplication.network.ErrorModel
import com.google.gson.Gson
import com.google.gson.JsonParser
import okhttp3.ResponseBody

object NetworkExtensions {
    fun getErrorMessage(responseBody: ResponseBody?): ErrorModel? {
        try {
            responseBody?.let {
                val json = JsonParser.parseString(it.string())
                return Gson().fromJson(json, ErrorModel::class.java)
            } ?: let {
                return null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}