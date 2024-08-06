package com.nabi.nabi.views.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val loginState: LiveData<UiState<Boolean>> get() = _loginState

    fun signIn(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                signInUseCase(idToken, provider)
                    .onSuccess {
                        withContext(Dispatchers.IO) {
                            dataStoreRepository.setAuthProvider(provider)
                            dataStoreRepository.setAccessToken(it.accessToken)
                        }
                        _loginState.value = UiState.Success(it.isRegistered)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Sign-in failed: ${e.message}")
                        _loginState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Sign-in exception: ${e.message}")
                _loginState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}