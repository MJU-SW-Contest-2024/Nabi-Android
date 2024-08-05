package com.nabi.nabi.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.home.HomeUseCase
import com.nabi.domain.usecase.notification.RegisterFcmTokenUseCase
import com.nabi.nabi.fcm.MyFirebaseMessagingService
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _homeState = MutableLiveData<UiState<HomeInfo>>(UiState.Loading)
    val homeState: LiveData<UiState<HomeInfo>> get() = _homeState

    private val _fcmState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val fcmState: LiveData<UiState<Unit>> get() = _fcmState

    fun fetchData() {
        _homeState.value = UiState.Loading

        viewModelScope.launch {
            val accessTokenResult = dataStoreRepository.getAccessToken()
            if (accessTokenResult.isSuccess) {
                val accessToken = accessTokenResult.getOrNull().orEmpty()

                homeUseCase(accessToken).onSuccess {
                    _homeState.value = UiState.Success(it)
                }.onFailure { e ->
                    _homeState.value = UiState.Failure(message = e.message.toString())
                }
            } else {
                _homeState.value = UiState.Failure(message = "Failed to get access token")
            }
        }
    }

    fun registerFcmToken() {
        _fcmState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()
                val fcmToken = MyFirebaseMessagingService().getRegistrationToken().orEmpty()

                registerFcmTokenUseCase(accessToken, fcmToken)
                    .onSuccess {
                        _fcmState.value = UiState.Success(Unit)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Sign-in failed: ${e.message}")
                        _fcmState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Sign-in exception: ${e.message}")
                _fcmState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}