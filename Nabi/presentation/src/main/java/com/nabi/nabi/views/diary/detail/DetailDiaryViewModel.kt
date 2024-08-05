package com.nabi.nabi.views.diary.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.bookmark.AddBookmarkUseCase
import com.nabi.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.nabi.domain.usecase.diary.GetDiaryDetailUseCase
import com.nabi.domain.usecase.diary.UpdateDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailDiaryViewModel @Inject constructor(
    private val getDiaryDetailUseCase: GetDiaryDetailUseCase,
    private val updateDiaryUseCase: UpdateDiaryUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _isBookmarked = MutableLiveData<Boolean>(false)
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private val _diaryState = MutableLiveData<UiState<DiaryInfo>>(UiState.Loading)
    val diaryState: LiveData<UiState<DiaryInfo>> get() = _diaryState

    private val _addState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val addState: LiveData<UiState<Unit>> get() = _addState

    private val _deleteState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val deleteState: LiveData<UiState<Unit>> get() = _deleteState

    private val _updateState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val updateState: LiveData<UiState<Unit>> get() = _updateState

    fun fetchData(diaryId: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            getDiaryDetailUseCase(accessToken, diaryId)
                .onSuccess {
                    _isBookmarked.value = it.isBookmarked
                    _diaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun updateDiary(id: Int, content: String) {
        _updateState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            updateDiaryUseCase(accessToken, id, content)
                .onSuccess {
                    _addState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun addBookmark(diaryId: Int){
        _addState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            addBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _isBookmarked.value = true
                    _addState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun deleteBookmark(diaryId: Int){
        _deleteState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            deleteBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _isBookmarked.value = false
                    _deleteState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _deleteState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}