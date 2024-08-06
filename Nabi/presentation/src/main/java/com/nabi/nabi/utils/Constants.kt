package com.nabi.nabi.utils

import android.os.Build

object Constants {
    val FCM_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    val AUDIO_PERMISSIONS = arrayOf(android.Manifest.permission.RECORD_AUDIO)
}