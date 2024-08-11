package com.nabi.nabi.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.bookmark.AddBookmarkUseCase
import com.nabi.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.home.HomeUseCase
import com.nabi.domain.usecase.notification.RegisterFcmTokenUseCase
import com.nabi.nabi.fcm.MyFirebaseMessagingService
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val registerFcmTokenUseCase: RegisterFcmTokenUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _homeState = MutableLiveData<UiState<HomeInfo>>(UiState.Loading)
    val homeState: LiveData<UiState<HomeInfo>> get() = _homeState

    private val _addState = MutableLiveData<UiState<Int>>(UiState.Loading)
    val addState: LiveData<UiState<Int>> get() = _addState

    private val _deleteState = MutableLiveData<UiState<Int>>(UiState.Loading)
    val deleteState: LiveData<UiState<Int>> get() = _deleteState

    fun fetchData() {
        _homeState.value = UiState.Loading

        viewModelScope.launch {
            val accessTokenResult = getAccessTokenUseCase.invoke()
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
        try {
            MyFirebaseMessagingService().getRegistrationToken { token ->
                if (token != null) {
                    viewModelScope.launch {
                        val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

                        registerFcmTokenUseCase(accessToken, token)
                            .onSuccess {
                                LoggerUtils.d("Register fcm success: $token")
                            }
                            .onFailure { e ->
                                LoggerUtils.e("Register fcm failed: ${e.message}")
                            }
                    }
                } else {
                    throw Exception("Null Fcm Token Exception")
                }
            }
        } catch (e: Exception) {
            LoggerUtils.e("Register fcm exception: ${e.message}")
        }
    }

    fun addBookmark(diaryId: Int) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            addBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _addState.value = UiState.Success(diaryId)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun deleteBookmark(diaryId: Int) {
        _deleteState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            deleteBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _deleteState.value = UiState.Success(diaryId)
                }.onFailure { e ->
                    _deleteState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}