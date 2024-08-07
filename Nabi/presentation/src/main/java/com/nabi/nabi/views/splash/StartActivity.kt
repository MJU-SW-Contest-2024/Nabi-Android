package com.nabi.nabi.views.splash

import android.content.Intent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.kakao.sdk.auth.AuthApiClient
import com.kakao.sdk.user.UserApiClient
import com.nabi.data.service.KakaoAuthService
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.nabi.R
import com.nabi.nabi.base.BaseActivity
import com.nabi.nabi.base.NabiApplication.Companion.nickname
import com.nabi.nabi.databinding.ActivityStartBinding
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import com.nabi.nabi.views.MainActivity
import com.nabi.nabi.views.sign.SignActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity: BaseActivity<ActivityStartBinding>(R.layout.activity_start) {
    private val viewModel: StartViewModel by viewModels()

    @Inject
    lateinit var kakaoAuthService: KakaoAuthService

    @Inject lateinit var dataStoreRepository: DataStoreRepository

    override fun initView() {
        viewModel.fetchMyInfo()
    }

    override fun setObserver() {
        super.setObserver()

        viewModel.uiState.observe(this){
            when(it){
                is UiState.Loading -> {}
                is UiState.Failure -> {
                    LoggerUtils.e("서버 토큰 유효하지 않음:\n${it.message}")
                    moveActivity(false)
                }
                is UiState.Success -> {
                    nickname = it.data
                    handleLoginSuccess()
                }
            }
        }
    }

    private fun handleLoginSuccess() {
        lifecycleScope.launch(Dispatchers.IO) {
            val provider = dataStoreRepository.getAuthProvider().getOrNull()

            when (provider) {
                AuthProvider.KAKAO -> checkKakaoToken()
                AuthProvider.GOOGLE -> {}
                AuthProvider.NAVER -> {}
                else -> moveActivity(false)
            }
        }
    }

    private fun checkKakaoToken() {
        if (!AuthApiClient.instance.hasToken()) {
            LoggerUtils.e("카카오 토큰 유효성 검사: 토큰 없음")
            moveActivity(false)
            return
        }

        UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
            if (error != null || tokenInfo == null) {
                LoggerUtils.e("카카오 토큰 유효성 검사 실패: ${error?.stackTraceToString()}")
                moveActivity(false)
            } else {
                LoggerUtils.d("카카오 토큰 유효성 검사 성공")
                moveActivity(true)
            }
        }
    }

    private fun moveActivity(isValid: Boolean) {
        val destination = if (isValid) {
            if (viewModel.isRegister) MainActivity::class.java else SignActivity::class.java
        } else {
            SignActivity::class.java
        }

        val intent = createIntent(destination)

        if(isValid && !viewModel.isRegister){
            intent.putExtra("isLoginSuccess", true)
        }

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun createIntent(destination: Class<*>) : Intent {
        val intent = Intent(this, destination)
        return intent
    }
}