package com.nabi.data.service

import android.content.Context
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nabi.data.utils.LoggerUtils
import com.nabi.domain.enums.AuthProvider
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.reflect.KFunction2

class KakaoAuthService @Inject constructor(
    @ActivityContext private val context: Context,
    private val client: UserApiClient,
) {

    companion object {
        const val KAKAO_TALK = "카카오톡"
        const val KAKAO_ACCOUNT = "카카오계정"
        const val KAKAO_ID_TOKEN = "카카오 ID 토큰"
    }

    private val isKakaoTalkLoginAvailable: Boolean
        get() = client.isKakaoTalkLoginAvailable(context)

    fun signInKakao(
        signInListener: KFunction2<String, AuthProvider, Unit>
    ) {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                signInError(error)
            } else if (token != null) signInSuccess(token, signInListener)
        }

        if (isKakaoTalkLoginAvailable) {
            client.loginWithKakaoTalk(context, callback = callback)
        } else {
            client.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun signInError(throwable: Throwable) {
        val kakaoType = if (isKakaoTalkLoginAvailable) KAKAO_TALK else KAKAO_ACCOUNT
        LoggerUtils.e("{$kakaoType}으로 로그인 실패 ${throwable.message}")
    }

    private fun signInSuccess(
        oAuthToken: OAuthToken,
        signInListener: KFunction2<String, AuthProvider, Unit>
    ) {
        LoggerUtils.d("$KAKAO_ID_TOKEN ${oAuthToken.idToken}")
        client.me { _, error ->
            signInListener(oAuthToken.idToken ?: "", AuthProvider.KAKAO)
            if (error != null) {
                LoggerUtils.e("사용자 정보 요청 실패 $error")
            }
        }
    }

    fun signOut(signOutListener: ((Throwable?) -> Unit)? = null) {
        client.logout { error ->
            if (error != null) {
                LoggerUtils.e("로그아웃 실패. SDK에서 토큰 삭제됨 $error")
            } else {
                LoggerUtils.i("로그아웃 성공. SDK에서 토큰 삭제됨")
            }
            signOutListener?.invoke(error)
        }
    }

    fun withdraw(withdrawListener: ((Throwable?) -> Unit)? = null) {
        client.unlink { error ->
            if (error != null) {
                LoggerUtils.e("회원탈퇴 실패 $error")
            } else {
                LoggerUtils.i("회원탈퇴 성공")
            }
            withdrawListener?.invoke(error)
        }
    }
}