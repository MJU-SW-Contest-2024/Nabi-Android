package com.nabi.domain.model.diary

data class DiarySelectInfo(
    var existDiary: Boolean = false,
    val diaryEntryDate: String,
    var isSelected: Boolean = false
)
