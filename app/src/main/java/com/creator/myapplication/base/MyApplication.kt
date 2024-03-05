package com.creator.myapplication.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    companion object {
        private lateinit var instance: MyApplication

        fun getAppContext(): MyApplication {
            return instance
        }
    }
}