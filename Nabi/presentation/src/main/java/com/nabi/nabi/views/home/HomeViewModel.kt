package com.nabi.nabi.views.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.home.HomeInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.home.HomeUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _homeState = MutableLiveData<UiState<HomeInfo>>(UiState.Loading)
    val homeState: LiveData<UiState<HomeInfo>> get() = _homeState

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
}