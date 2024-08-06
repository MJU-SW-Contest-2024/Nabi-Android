package com.nabi.nabi.views.diary.statistics

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.emotion.EmotionStatistics
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.domain.usecase.emotion.GetEmotionStatisticsUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DiaryStatisticsViewModel @Inject constructor(
    private val getEmotionStatisticsUseCase: GetEmotionStatisticsUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<UiState<EmotionStatistics>>(UiState.Loading)
    val uiState: LiveData<UiState<EmotionStatistics>> get() = _uiState

    fun fetchData(startDate: String, endDate: String) {
        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            getEmotionStatisticsUseCase(accessToken, startDate, endDate)
                .onSuccess {
                    _uiState.value = UiState.Success(it)
                }.onFailure { e ->
                    _uiState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}