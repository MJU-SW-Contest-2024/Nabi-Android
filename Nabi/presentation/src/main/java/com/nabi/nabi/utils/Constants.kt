package com.nabi.nabi.utils

import android.os.Build
import java.text.SimpleDateFormat
import java.util.Locale

object Constants {
    val FCM_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arrayOf(android.Manifest.permission.POST_NOTIFICATIONS)
    } else {
        emptyArray()
    }

    val AUDIO_PERMISSIONS = arrayOf(android.Manifest.permission.RECORD_AUDIO)

    val dateDashFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
    val dateKoreanFormat = SimpleDateFormat("yyyy년 M월 d일", Locale.KOREAN)
    val dateEnglishOnlyYearFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    val dateEnglishOnlyMonthFormat = SimpleDateFormat("MMMM", Locale.ENGLISH)
    val dateNumberOnlyMonthFormat = SimpleDateFormat("M", Locale.getDefault())
}