package com.nabi.nabi.views.diary.emotion

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.emotion.SearchEmotionUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmotionSearchViewModel @Inject constructor(
    private val searchEmotionUseCase: SearchEmotionUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private var page = 0
    private val size = 15
    private val sort = ""
    private var isFinish = false

    private var _searchEmotion = MutableLiveData<String>("")
    val searchEmotion: LiveData<String> get() = _searchEmotion

    private val _uiState = MutableLiveData<UiState<List<SearchDiary>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<SearchDiary>>> get() = _uiState

    private val _diaryItems = MutableLiveData<MutableList<SearchDiary>>(mutableListOf())
    val diaryItems: LiveData<MutableList<SearchDiary>> get() = _diaryItems

    fun fetchData(emotion: String, isNewEmotion: Boolean = false) {
        if(searchEmotion.value != emotion) resetPageable(emotion)
        if(!isNewEmotion && isFinish) return

        _uiState.value = UiState.Loading
        _searchEmotion.value = emotion

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            searchEmotionUseCase(accessToken, emotion, page, size, sort)
                .onSuccess {
                    isFinish = it.first.isLastPage
                    page++

                    _diaryItems.value?.addAll(it.second)
                    _uiState.value = UiState.Success(it.second)
                }.onFailure { e ->
                    _uiState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    fun resetPageable(content: String){
        _diaryItems.value?.clear()
        _searchEmotion.value = content
        page = 0
        isFinish = false
    }
}