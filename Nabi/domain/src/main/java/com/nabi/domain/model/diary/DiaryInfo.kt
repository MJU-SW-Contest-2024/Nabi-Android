package com.nabi.domain.model.diary

data class DiaryInfo(
    val content: String,
    val diaryEntryDate: String,
    val diaryId: Int,
    val emotion: String?,
    val isBookmarked: Boolean,
    val nickname: String
)