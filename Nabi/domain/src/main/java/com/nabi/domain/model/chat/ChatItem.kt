package com.nabi.domain.model.chat

data class ChatItem(
    val chatId: Int,
    val date: String,
    val time: String,
    val originalDateTime: String,
    val content: String,
    val isMine: Boolean,
    val isDateHeader: Boolean = false,
    var showRefreshIcon: Boolean = false
)
