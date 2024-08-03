package com.nabi.nabi.views.diary.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.diary.GetMonthlyDiaryUseCase
import com.nabi.domain.usecase.diary.SearchDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDiaryViewModel @Inject constructor(
    private val searchDiaryUseCase: SearchDiaryUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private var page = 0
    private val size = 15
    private val sort = ""
    private var isFinish = false

    private val _diaryState = MutableLiveData<UiState<List<SearchDiary>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<SearchDiary>>> get() = _diaryState

    fun fetchData(content: String) {
        if(isFinish) return

        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            searchDiaryUseCase(accessToken, content, page, size, sort)
                .onSuccess {
                    isFinish = it.first.totalPages != page

                    _diaryState.value = UiState.Success(it.second)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}