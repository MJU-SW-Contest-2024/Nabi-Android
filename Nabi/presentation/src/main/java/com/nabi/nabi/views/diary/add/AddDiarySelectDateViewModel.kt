package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryDbEntity
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.DiarySelectInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.repository.DiaryDbRepository
import com.nabi.domain.usecase.diary.AddTempDiaryUseCase
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.domain.usecase.diary.GetTempDiaryUseCase
import com.nabi.domain.usecase.diary.UpdateTempDiaryUseCase
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiarySelectDateViewModel @Inject constructor(
    private val getMonthlyDiaryUseCase: GetMonthlyDiaryUseCase,
    private val getTempDiaryUseCase: GetTempDiaryUseCase,
    private val dataStoreRepository: DataStoreRepository,
) : ViewModel() {
    private val _diaryState = MutableLiveData<UiState<List<DiarySelectInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DiarySelectInfo>>> get() = _diaryState

    private val _diaryDates = MutableLiveData<Set<String>>()
    val diaryDates: LiveData<Set<String>> get() = _diaryDates

    private val _getTempState = MutableLiveData<UiState<DiaryDbEntity>>(UiState.Loading)
    val getTempState: LiveData<UiState<DiaryDbEntity>> get() = _getTempState

    fun setDiaryDates(dates: Set<String>) {
        _diaryDates.value = dates
    }

    fun checkMonthDiary(year: Int, month: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            getMonthlyDiaryUseCase(accessToken, year, month).onSuccess {
                _diaryState.value = UiState.Success(
                    it.map { data ->
                        DiarySelectInfo(
                            diaryEntryDate = data.diaryEntryDate
                        )
                    }
                )
            }.onFailure { e ->
                _diaryState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }

    fun getTempDiary(date: String) {
        _getTempState.value = UiState.Loading

        viewModelScope.launch {
            getTempDiaryUseCase(date).onSuccess {
                if (it != null) {
                    _getTempState.value = UiState.Success(it)
                } else {
                    _getTempState.value = UiState.Success(DiaryDbEntity(date, "null"))
                }
            }.onFailure { e ->
                _getTempState.value = UiState.Failure(message = e.message.toString())
            }
        }
    }
}