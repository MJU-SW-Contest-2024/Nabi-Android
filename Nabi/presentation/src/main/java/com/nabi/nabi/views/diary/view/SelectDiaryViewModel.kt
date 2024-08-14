package com.nabi.nabi.views.diary.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SelectDiaryViewModel @Inject constructor(
    private val getMonthlyDiaryUseCase: GetMonthlyDiaryUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {
    var isUpdateFlag = false

    private val _diaryState = MutableLiveData<UiState<List<DiaryInfo>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<DiaryInfo>>> get() = _diaryState

    fun fetchData(year: Int, month: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            getMonthlyDiaryUseCase(accessToken, year, month)
                .onSuccess {
                    _diaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}