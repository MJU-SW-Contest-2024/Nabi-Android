package com.nabi.nabi.views.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.user.GetUserInfoUseCase
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    var isRegister = false

    private val _uiState = MutableLiveData<UiState<String>>(UiState.Loading)
    val uiState: LiveData<UiState<String>> get() = _uiState

    fun fetchMyInfo() {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            try {
                val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

                getUserInfoUseCase(accessToken)
                    .onSuccess {
                        isRegister = it.isRegistered
                        _uiState.value = UiState.Success(it.nickname)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Token Validation failed: ${e.message}")
                        _uiState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Token Validation exception: ${e.message}")
                _uiState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}
