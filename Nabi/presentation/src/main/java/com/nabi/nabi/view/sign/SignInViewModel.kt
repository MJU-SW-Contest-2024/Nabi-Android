package com.nabi.nabi.view.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.nabi.base.NabiApplication.Companion.application
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val loginState: LiveData<UiState<Unit>> get() = _loginState

    fun signIn(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            signInUseCase(
                idToken, provider
            ).onSuccess {
                runBlocking(Dispatchers.IO) {
                    application.dataStore.setAuthProvider(provider)
                    application.dataStore.setAccessToken(it.accessToken)
                }
                _loginState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _loginState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}