package com.nabi.nabi.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.Utility
import com.nabi.nabi.BuildConfig
import com.nabi.nabi.utils.LoggerUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class NabiApplication : Application() {
    companion object {
        lateinit var application: NabiApplication
        var nickname: String? = null
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