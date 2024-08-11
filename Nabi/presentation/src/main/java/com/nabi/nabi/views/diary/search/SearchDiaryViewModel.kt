package com.nabi.nabi.views.diary.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.diary.SearchDiary
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.datastore.GetAccessTokenUseCase
import com.nabi.domain.usecase.diary.SearchDiaryUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchDiaryViewModel @Inject constructor(
    private val searchDiaryUseCase: SearchDiaryUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {

    private var page = 0
    private val size = 15
    private val sort = ""
    private var isFinish = false

    private var _searchWord = MutableLiveData<String>("")
    val searchWord: LiveData<String> get() = _searchWord

    private val _diaryState = MutableLiveData<UiState<List<SearchDiary>>>(UiState.Loading)
    val diaryState: LiveData<UiState<List<SearchDiary>>> get() = _diaryState

    private val _diaryItems = MutableLiveData<MutableList<SearchDiary>>(mutableListOf())
    val diaryItems: LiveData<MutableList<SearchDiary>> get() = _diaryItems

    fun fetchData(content: String = searchWord.value!!) {
        if(searchWord.value != content) resetPageable(content)

        if(isFinish) return

        _diaryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = getAccessTokenUseCase.invoke().getOrNull().orEmpty()

            searchDiaryUseCase(accessToken, content, page, size, sort)
                .onSuccess {
                    isFinish = it.first.isLastPage
                    page++

                    _diaryItems.value?.addAll(it.second)
                    _diaryState.value = UiState.Success(it.second)
                }.onFailure { e ->
                    _diaryState.value = UiState.Failure(message = e.message.toString())
                }
        }
    }

    private fun resetPageable(content: String){
        _diaryItems.value?.clear()
        _searchWord.value = content
        page = 0
        isFinish = false
    }
}