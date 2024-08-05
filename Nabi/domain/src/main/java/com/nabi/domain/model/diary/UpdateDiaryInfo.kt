package com.nabi.domain.model.diary

data class UpdateDiaryInfo(
    val diaryId: Int,
    val content: String,
    val diaryEntryDate: String
)
