package com.nabi.nabi.views.myPage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.usecase.auth.WithdrawUseCase
import com.nabi.domain.usecase.datastore.ClearUserDataUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel @Inject constructor(
    private val withdrawUseCase: WithdrawUseCase,
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private val _withdrawState = MutableLiveData<UiState<String>>(UiState.Loading)
    val withdrawState: LiveData<UiState<String>> get() = _withdrawState

    private val _clearState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val clearState: LiveData<UiState<Boolean>> get() = _clearState


    fun withdraw() {
        _withdrawState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            try {
                withdrawUseCase(accessToken)
                    .onSuccess {
                        _withdrawState.value = UiState.Success(it)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Withdraw failed: ${e.message}")
                        _withdrawState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Withdraw exception: ${e.message}")
                _withdrawState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    fun clearData() {
        _clearState.value = UiState.Loading

        viewModelScope.launch {
            try {
                clearUserDataUseCase()
                    .onSuccess {
                        _clearState.value = UiState.Success(it)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Clear User Data failed: ${e.message}")
                        _clearState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Clear User Data: ${e.message}")
                _clearState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}