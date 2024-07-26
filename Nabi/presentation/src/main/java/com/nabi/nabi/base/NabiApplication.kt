package com.nabi.nabi.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NabiApplication : Application() {
    companion object {
        lateinit var app: NabiApplication
        var userName: String? = null
    }
}