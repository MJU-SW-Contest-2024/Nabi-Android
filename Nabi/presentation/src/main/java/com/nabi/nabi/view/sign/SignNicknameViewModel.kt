package com.nabi.nabi.view.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.enums.AuthProvider
import com.nabi.domain.usecase.auth.SetNicknameUseCase
import com.nabi.domain.usecase.auth.SignInUseCase
import com.nabi.nabi.base.NabiApplication.Companion.application
import com.nabi.nabi.di.dataStore
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SignNicknameViewModel @Inject constructor(
    private val setNicknameUseCase: SetNicknameUseCase
): ViewModel() {

    private val _nickState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val nickState: LiveData<UiState<Unit>> get() = _nickState

    fun setNickname(nick: String) {
        _nickState.value = UiState.Loading

        viewModelScope.launch {
            setNicknameUseCase(
                application.dataStore.getAccessToken().getOrNull().orEmpty(),
                nick
            ).onSuccess {
                _nickState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _nickState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}