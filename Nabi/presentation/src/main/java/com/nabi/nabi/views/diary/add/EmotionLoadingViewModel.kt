package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.emotion.AddDiaryEmotionUseCase
import com.nabi.domain.usecase.emotion.GetDiaryEmotionUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmotionLoadingViewModel @Inject constructor(
    private val getDiaryEmotionUseCase: GetDiaryEmotionUseCase,
    private val addDiaryEmotionUseCase: AddDiaryEmotionUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _getEmotionState = MutableLiveData<UiState<String>>(UiState.Loading)
    val getEmotionState: LiveData<UiState<String>> get() = _getEmotionState

    private val _addEmotionState = MutableLiveData<UiState<AddDiaryEmotionMsg>>(UiState.Loading)
    val addEmotionState: LiveData<UiState<AddDiaryEmotionMsg>> get() = _addEmotionState

    fun getDiaryEmotion(diaryId: Int) {
        _getEmotionState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            getDiaryEmotionUseCase(accessToken, diaryId)
                .onSuccess {
                    _getEmotionState.value = UiState.Success(it)
                }.onFailure { e ->
                    _getEmotionState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun addDiaryEmotion(diaryId: Int, emotionState: String) {
        _addEmotionState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            addDiaryEmotionUseCase(accessToken, diaryId, emotionState)
                .onSuccess {
                    _addEmotionState.value = UiState.Success(it)
                }.onFailure { e ->
                    _addEmotionState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}