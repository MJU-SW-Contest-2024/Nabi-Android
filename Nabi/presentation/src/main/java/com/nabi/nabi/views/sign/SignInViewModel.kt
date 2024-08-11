package com.nabi.nabi.views.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.model.auth.SignInInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.datastore.GetRecentAuthProviderUseCase
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.datastore.SaveSignInInfoUseCase
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
    private val saveSignInInfoUseCase: SaveSignInInfoUseCase,
    private val getRecentAuthProviderUseCase: GetRecentAuthProviderUseCase,
) : ViewModel() {

    private val _loginState = MutableLiveData<UiState<Pair<AuthProvider, SignInInfo>>>(UiState.Loading)
    val loginState: LiveData<UiState<Pair<AuthProvider, SignInInfo>>> get() = _loginState

    private val _saveState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val saveState: LiveData<UiState<Boolean>> get() = _saveState

    private val _recentState = MutableLiveData<UiState<AuthProvider>>(UiState.Loading)
    val recentState : LiveData<UiState<AuthProvider>> get() = _recentState


    fun signIn(idToken: String, provider: AuthProvider) {
        _loginState.value = UiState.Loading

        viewModelScope.launch {
            try {
                signInUseCase(idToken, provider)
                    .onSuccess {
                        _loginState.value = UiState.Success(Pair(provider, it))
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

    fun saveSignInInfo(info: Pair<AuthProvider, SignInInfo>){
        _saveState.value = UiState.Loading

        viewModelScope.launch {
            try {
                saveSignInInfoUseCase(info.second.accessToken, "", info.first)
                    .onSuccess {
                        _saveState.value = UiState.Success(info.second.isRegistered)
                    }
                    .onFailure { e ->
                        LoggerUtils.e("Sign-in failed: ${e.message}")
                        _saveState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                LoggerUtils.e("Sign-in exception: ${e.message}")
                _saveState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    fun getRecentAuthProvider() {
        _recentState.value = UiState.Loading

        viewModelScope.launch {
            try {
                getRecentAuthProviderUseCase()
                    .onSuccess {
                        _recentState.value = UiState.Success(it)
                    }
                    .onFailure { e ->
                        _recentState.value = UiState.Failure(message = e.message.toString())
                    }
            } catch (e: Exception) {
                _recentState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}