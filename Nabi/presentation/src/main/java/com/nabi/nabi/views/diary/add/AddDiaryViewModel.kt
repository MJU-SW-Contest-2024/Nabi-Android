package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.AddDiaryInfo
import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.model.diary.UpdateDiaryInfo
import com.nabi.domain.model.emotion.AddDiaryEmotionMsg
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.repository.DiaryDbRepository
import com.nabi.domain.usecase.diary.AddDiaryUseCase
import com.nabi.domain.usecase.diary.AddTempDiaryUseCase
import com.nabi.domain.usecase.diary.GetTempDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateTempDiaryUseCase
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
    private val getTempDiaryUseCase: GetTempDiaryUseCase,
    private val addTempDiaryUseCase: AddTempDiaryUseCase,
    private val updateTempDiaryUseCase: UpdateTempDiaryUseCase,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {

    private val _addState = MutableLiveData<UiState<AddDiaryInfo>>(UiState.Loading)
    val addState: LiveData<UiState<AddDiaryInfo>> get() = _addState

    private val _updateState = MutableLiveData<UiState<UpdateDiaryInfo>>(UiState.Loading)
    val updateState: LiveData<UiState<UpdateDiaryInfo>> get() = _updateState

    private val _getTempState = MutableLiveData<UiState<Boolean>>(UiState.Loading)
    val getTempState: LiveData<UiState<Boolean>> get() = _getTempState

    private val _addTempState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val addTempState: LiveData<UiState<Unit>> get() = _addTempState

    private val _updateTempState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val updateTempState: LiveData<UiState<Unit>> get() = _updateTempState

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

    fun getTempDiary(date: String) {
        _getTempState.value = UiState.Loading

        viewModelScope.launch {
            getTempDiaryUseCase(date).onSuccess {
                if (it?.diaryTempContent != null) {
                    _getTempState.value = UiState.Success(true)
                } else {
                    _getTempState.value = UiState.Success(false)
                }
            }.onFailure { e ->
                _getTempState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    fun addTempDiary(diary: DiaryDbEntity) {
        _addTempState.value = UiState.Loading

        viewModelScope.launch {
            addTempDiaryUseCase(diary)
                .onSuccess {
                    _addTempState.value = UiState.Success(it)
                }.onFailure { e ->
                    _addTempState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun updateTempDiary(diary: DiaryDbEntity) {
        _updateTempState.value = UiState.Loading

        viewModelScope.launch {
            updateTempDiaryUseCase(diary.diaryTempDate, diary.diaryTempContent)
                .onSuccess {
                    _updateTempState.value = UiState.Success(it)
                }.onFailure { e ->
                    _updateTempState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}