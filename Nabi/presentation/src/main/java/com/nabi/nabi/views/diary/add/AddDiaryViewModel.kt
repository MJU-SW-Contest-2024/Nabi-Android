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
    private val getMonthlyDiaryUseCase: GetMonthlyDiaryUseCase,
    private val addDiaryUseCase: AddDiaryUseCase,

    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    val selectedDate: LiveData<String> get() = _selectedDate

    private val _diaryState = MutableLiveData<UiState<List<DiaryInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DiaryInfo>>> get() = _diaryState

    private val _addState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val addState: LiveData<UiState<Unit>> get() = _addState

    fun selectDate(date: String) {
        _selectedDate.value = date
    }

    fun checkMonthDiary(year: Int, month: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessTokenResult = dataStoreRepository.getAccessToken()
            if (accessTokenResult.isSuccess) {
                val accessToken = accessTokenResult.getOrNull().orEmpty()

                getMonthlyDiaryUseCase(accessToken, year, month).onSuccess {
                    _diaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
            } else {
                _diaryState.value = UiState.Failure(message = "Failed to get access token")
            }
        }
    }

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

}