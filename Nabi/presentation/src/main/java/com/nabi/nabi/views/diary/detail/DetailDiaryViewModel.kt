package com.nabi.nabi.views.diary.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.DeleteDiaryMsg
import com.nabi.domain.model.diary.DiaryInfo
import com.nabi.domain.usecase.bookmark.AddBookmarkUseCase
import com.nabi.domain.usecase.bookmark.DeleteBookmarkUseCase
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.diary.DeleteDiaryUseCase
import com.nabi.domain.usecase.diary.GetDiaryDetailUseCase
import com.nabi.domain.usecase.emotion.PatchDiaryEmotionUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailDiaryViewModel @Inject constructor(
    private val getDiaryDetailUseCase: GetDiaryDetailUseCase,
    private val addBookmarkUseCase: AddBookmarkUseCase,
    private val deleteBookmarkUseCase: DeleteBookmarkUseCase,
    private val deleteDiaryUseCase: DeleteDiaryUseCase,
    private val patchDiaryEmotionUseCase: PatchDiaryEmotionUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase,
) : ViewModel() {

    private val _isBookmarked = MutableLiveData(false)
    val isBookmarked: LiveData<Boolean> get() = _isBookmarked

    private val _diaryState = MutableLiveData<UiState<DiaryInfo>>(UiState.Loading)
    val diaryState: LiveData<UiState<DiaryInfo>> get() = _diaryState

    private val _addState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val addState: LiveData<UiState<Unit>> get() = _addState

    private val _deleteState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val deleteState: LiveData<UiState<Unit>> get() = _deleteState

    private val _deleteDiaryState = MutableLiveData<UiState<DeleteDiaryMsg>>(UiState.Loading)
    val deleteDiaryState: LiveData<UiState<DeleteDiaryMsg>> get() = _deleteDiaryState

    private val _patchState = MutableLiveData<UiState<String>>(UiState.Loading)
    val patchState: LiveData<UiState<String>> get() = _patchState

    var currentEmotion: String = ""

    fun fetchData(diaryId: Int) {
        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            getDiaryDetailUseCase(accessToken, diaryId)
                .onSuccess {
                    currentEmotion = it.emotion ?: ""
                    _isBookmarked.value = it.isBookmarked
                    _diaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun addBookmark(diaryId: Int) {
        _addState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            addBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _isBookmarked.value = true
                    _addState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _addState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun deleteBookmark(diaryId: Int) {
        _deleteState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            deleteBookmarkUseCase(accessToken, diaryId)
                .onSuccess {
                    _isBookmarked.value = false
                    _deleteState.value = UiState.Success(Unit)
                }.onFailure { e ->
                    _deleteState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun deleteDiary(diaryId: Int) {
        _deleteDiaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            deleteDiaryUseCase(accessToken, diaryId)
                .onSuccess {
                    _deleteDiaryState.value = UiState.Success(it)
                }.onFailure { e ->
                    _deleteDiaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun patchDiaryEmotion(diaryId: Int, emotion: String){
        _patchState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            patchDiaryEmotionUseCase(accessToken, diaryId, emotion)
                .onSuccess {
                    _patchState.value = UiState.Success(it)
                }.onFailure { e ->
                    _patchState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }
}