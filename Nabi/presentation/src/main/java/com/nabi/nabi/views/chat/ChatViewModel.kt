package com.nabi.nabi.views.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nabi.domain.model.chat.ChatItem
import com.nabi.domain.repository.DataStoreRepository
import com.nabi.domain.usecase.chat.EmbeddingDiaryUseCase
import com.nabi.domain.usecase.chat.GetChatHistoryUseCase
import com.nabi.domain.usecase.chat.RetryChatResUseCase
import com.nabi.domain.usecase.chat.SendChatUseCase
import com.nabi.nabi.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val embeddingDiaryUseCase: EmbeddingDiaryUseCase,
    private val sendChatUseCase: SendChatUseCase,
    private val getChatHistoryUseCase: GetChatHistoryUseCase,
    private val retryChatResUseCase: RetryChatResUseCase,
    private val dataStoreRepository: DataStoreRepository
): ViewModel() {
    private var page = 0
    private val size = 50
    private val sort = ""
    private var isFinish = false

    private val _embedState = MutableLiveData<UiState<Unit>>(UiState.Loading)
    val embedState: LiveData<UiState<Unit>> = _embedState

    private val _retryState = MutableLiveData<UiState<Int>>(UiState.Loading)
    val retryState: LiveData<UiState<Int>> = _retryState

    private val _uiState = MutableLiveData<UiState<List<ChatItem>>>(UiState.Loading)
    val uiState: LiveData<UiState<List<ChatItem>>> = _uiState

    private val _resState = MutableLiveData<UiState<String>>(UiState.Loading)
    val resState: LiveData<UiState<String>> = _resState

    fun fetchChatHistory(isReset: Boolean = false){
        if(isReset) resetPageable()
        else if(isFinish) return

        _uiState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            getChatHistoryUseCase.invoke(accessToken, page, size, sort)
                .onSuccess {
                    isFinish = it.first.isLastPage
                    page++

                    _uiState.value = UiState.Success(it.second)
                }
                .onFailure {
                    _uiState.value = UiState.Failure(message = it.message.toString())
                }
        }
    }

    private fun resetPageable(){
        page = 0
        isFinish = false
    }

    fun sendQuestion(question: String){
        _resState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            sendChatUseCase.invoke(accessToken, question)
                .onSuccess {
                    _resState.value = UiState.Success(it)
                }
                .onFailure {
                    _resState.value = UiState.Failure(message = it.message.toString())
                }
        }
    }

    fun embedDiary(){
        _embedState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            embeddingDiaryUseCase.invoke(accessToken)
                .onSuccess {
                    _embedState.value = UiState.Success(Unit)
                }
                .onFailure {
                    _embedState.value = UiState.Failure(message = it.message.toString())
                }
        }
    }

    fun retryChatRes(itemPos: Int){
        _retryState.value = UiState.Loading

        viewModelScope.launch {
            val accessToken = dataStoreRepository.getAccessToken().getOrNull().orEmpty()

            retryChatResUseCase.invoke(accessToken)
                .onSuccess {
                    _retryState.value = UiState.Success(itemPos)
                }
                .onFailure {
                    _retryState.value = UiState.Failure(message = it.message.toString())
                }
        }
    }
}