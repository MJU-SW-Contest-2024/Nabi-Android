package com.nabi.nabi.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.BuildConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NabiApplication : Application() {
    companion object {
        lateinit var application: NabiApplication
    }

    override fun onCreate() {
        super.onCreate()

        application = this
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)

        getHashKey()
    }

    private fun getHashKey(){
        val keyHash = Utility.getKeyHash(this)
        LoggerUtils.i("KeyHash: $keyHash")
    }
}