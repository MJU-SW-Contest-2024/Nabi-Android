package com.nabi.data.model.diary

data class UpdateDiaryRequestDTO (
    val content: String,
    val diaryEntryDate: String
)