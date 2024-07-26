package com.nabi.nabi.base

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.nabi.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class NabiApplication : Application() {
    companion object {
        lateinit var application: NabiApplication
    }

    @Inject
    lateinit var dataStore: DataStoreRepository

    override fun onCreate() {
        super.onCreate()

        application = this
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_KEY)
    }
}