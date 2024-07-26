package com.nabi.nabi.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nabi.nabi.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NabiApplication : Application() {
    companion object {
        lateinit var application: NabiApplication
    }

    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }
}