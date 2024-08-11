package com.nabi.nabi.views.sign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.auth.SetNicknameUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignNicknameViewModel @Inject constructor(
    private val setNicknameUseCase: SetNicknameUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
): ViewModel() {

    private val _nickState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val nickState: LiveData<UiState<Unit>> get() = _nickState

    fun setNickname(nick: String) {
        _nickState.value = UiState.Loading

        viewModelScope.launch {
            setNicknameUseCase(
                getAccessTokenUseCase.invoke().getOrNull().orEmpty(),
                nick
            ).onSuccess {
                _nickState.value = UiState.Success(Unit)
            }.onFailure { e ->
                _nickState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}