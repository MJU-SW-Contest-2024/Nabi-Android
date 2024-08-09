package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.AddDiaryInfo
import com.nabi.domain.model.diary.UpdateDiaryInfo
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.AddDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateDiaryUseCase
import com.nabi.domain.usecase.emotion.AddDiaryEmotionUseCase
import com.nabi.domain.usecase.emotion.GetDiaryEmotionUseCase
import com.nabi.domain.utils.EmotionStateUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val addDiaryUseCase: AddDiaryUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
    private val getDiaryEmotionUseCase: GetDiaryEmotionUseCase,
    private val addDiaryEmotionUseCase: AddDiaryEmotionUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _addState = MutableLiveData<UiState<AddDiaryInfo>>(UiState.Loading)
    val addState: LiveData<UiState<AddDiaryInfo>> get() = _addState

    private val _updateState = MutableLiveData<UiState<UpdateDiaryInfo>>(UiState.Loading)
    val updateState: LiveData<UiState<UpdateDiaryInfo>> get() = _updateState

    private val _getEmotionState = MutableLiveData<UiState<String>>(UiState.Loading)
    val getEmotionState: LiveData<UiState<String>> get() = _getEmotionState

    private val _addEmotionState = MutableLiveData<UiState<AddDiaryEmotionMsg>>(UiState.Loading)
    val addEmotionState: LiveData<UiState<AddDiaryEmotionMsg>> get() = _addEmotionState

    fun addDiary(content: String, diaryEntryDate: String) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            addDiaryUseCase(accessToken, content, diaryEntryDate)
                .onSuccess {
                    _addState.value = UiState.Success(it)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun updateDiary(id: Int, content: String, diaryEntryDate: String) {
        _updateState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            updateDiaryUseCase(accessToken, id, content, diaryEntryDate)
                .onSuccess {
                    _updateState.value = UiState.Success(it)
                }.onFailure { e ->
                    _updateState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun getDiaryEmotion(diaryId: Int) {
        _getEmotionState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

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
            val emotion = EmotionStateUtils.parseEmotionState(emotionState)

            addDiaryEmotionUseCase(accessToken, diaryId, emotion)
                .onSuccess {
                    _addEmotionState.value = UiState.Success(it)
                }.onFailure { e ->
                    _addEmotionState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

}