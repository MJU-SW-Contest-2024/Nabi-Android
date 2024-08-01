package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.data.utils.LoggerUtils
import com.nabi.domain.model.diary.MonthDiaryInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.DiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val diaryUseCase: DiaryUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {
    private val _diaryState = MutableLiveData<UiState<List<MonthDiaryInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<MonthDiaryInfo>>> get() = _diaryState

    fun checkMonthDiary(year: Int, month: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessTokenResult = dataStoreRepository.getAccessToken()
            if (accessTokenResult.isSuccess) {
                val accessToken = accessTokenResult.getOrNull().orEmpty()

                diaryUseCase(accessToken, year, month).onSuccess {
                    _diaryState.value = UiState.Success(it)
                    it.forEach { diaryInfo ->
                        LoggerUtils.d(diaryInfo.toString())
                    }
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
            } else {
                _diaryState.value = UiState.Failure(message = "Failed to get access token")
            }
        }
    }
}