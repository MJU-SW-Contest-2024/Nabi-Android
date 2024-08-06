package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.AddDiaryUseCase
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiaryViewModel @Inject constructor(
    private val addDiaryUseCase: AddDiaryUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _addState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val addState: LiveData<UiState<Unit>> get() = _addState

    private val _updateState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val updateState: LiveData<UiState<Unit>> get() = _updateState

    fun addDiary(content: String, diaryEntryDate: String) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            addDiaryUseCase(accessToken, content, diaryEntryDate)
                .onSuccess {
                    _addState.value = UiState.Success(Unit)
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
                    _addState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}