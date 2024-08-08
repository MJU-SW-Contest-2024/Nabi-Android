package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.nabi.utils.LoggerUtils
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddDiarySelectDateViewModel @Inject constructor(
    private val getMonthlyDiaryUseCase: GetMonthlyDiaryUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _diaryState = MutableLiveData<UiState<List<DiaryInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DiaryInfo>>> get() = _diaryState

    private val _tempData = MutableLiveData<UiState<Pair<String, String>>>()
    val tempData: LiveData<UiState<Pair<String, String>>> get() = _tempData

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

    fun checkTempData(selectedDate: String) {
        _tempData.value = UiState.Loading
        viewModelScope.launch {
            val tempDataResult = dataStoreRepository.getTempData()
            val tempData = tempDataResult.getOrNull()?.takeIf { it.first == selectedDate }

            if (tempData != null) {
                _tempData.value = UiState.Success(tempData)
            } else {
                _tempData.value = UiState.Failure(message = "dataStore 조회 실패")
            }
        }
    }
}