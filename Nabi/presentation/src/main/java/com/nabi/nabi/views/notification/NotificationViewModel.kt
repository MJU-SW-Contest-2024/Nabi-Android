package com.nabi.nabi.views.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.notification.GetNotificationUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val getNotificationUseCase: GetNotificationUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _notifyState = MutableLiveData<UiState<List<String>>>(UiState.Loading)
    val notifyState: LiveData<UiState<List<String>>> get() = _notifyState

    fun fetchData() {
        _notifyState.value = UiState.Loading

        viewModelScope.launch {
            val accessTokenResult = getAccessTokenUseCase.invoke()

            if (accessTokenResult.isSuccess) {
                val accessToken = accessTokenResult.getOrNull().orEmpty()

                getNotificationUseCase(accessToken).onSuccess {
                    _notifyState.value = UiState.Success(it)
                }.onFailure { e ->
                    _notifyState.value = UiState.Failure(message = e.message.toString())
                }
            } else {
                _notifyState.value = UiState.Failure(message = "Failed to get Notification List")
            }
        }
    }
}