package com.nabi.data.service

import android.app.Activity
import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.user.UserApiClient
import com.nabi.domain.enums.AuthProvider
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject
import kotlin.reflect.KFunction2

class NaverAuthService @Inject constructor(
    @ActivityContext private val context: Context,
    private val client: UserApiClient,
) {

    companion object {
        const val NAVER_ID_TOKEN = "네이버 ID 토큰"
    }

    fun signInNaver(
        signInListener: (String, AuthProvider) -> Unit
    ) {
        val activity = context as? Activity
        if (activity == null) {
            Log.e("NAVER", "Context is not an Activity")
            return
        }

        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                val accessToken = NaverIdLoginSDK.getAccessToken()
                signInListener(accessToken ?: "", AuthProvider.NAVER)

                Log.d("NAVER", "로그인 성공")
                Log.d("NAVER", "AccessToken -> $accessToken")
                Log.d("NAVER", "RefreshToken -> ${NaverIdLoginSDK.getRefreshToken()}")
                Log.d("NAVER", "Expires -> ${NaverIdLoginSDK.getExpiresAt()}")
                Log.d("NAVER", "Type -> ${NaverIdLoginSDK.getTokenType()}")
                Log.d("NAVER", "State -> ${NaverIdLoginSDK.getState()}")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                Log.e("NAVER", "로그인 실패: $errorCode, $errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }

        NaverIdLoginSDK.authenticate(context, oauthLoginCallback)
    }

    fun signOut(signOutListener: ((Throwable?) -> Unit)? = null) {
        client.logout { error ->
            if (error != null) {
                Log.e("NAVER", "로그아웃 실패. SDK에서 토큰 삭제됨 $error")
            } else {
                Log.i("NAVER", "로그아웃 성공. SDK에서 토큰 삭제됨")
            }
            signOutListener?.invoke(error)
        }
    }

}