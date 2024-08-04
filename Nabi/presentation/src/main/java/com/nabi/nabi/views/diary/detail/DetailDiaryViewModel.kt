package com.nabi.nabi.views.diary.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.GetDiaryDetailUseCase
import com.nabi.domain.usecase.diary.SearchDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailDiaryViewModel @Inject constructor(
    private val getDiaryDetailUseCase: GetDiaryDetailUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _diaryState = MutableLiveData<UiState<DiaryInfo>>(UiState.Loading)
    val diaryState: LiveData<UiState<DiaryInfo>> get() = _diaryState

    fun fetchData(diaryId: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            getDiaryDetailUseCase(accessToken, diaryId)
                .onSuccess {
                    _diaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}