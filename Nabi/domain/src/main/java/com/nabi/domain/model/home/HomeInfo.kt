package com.nabi.domain.model.home

data class HomeInfo(
    val consecutiveWritingDays: Int,
    val nickname: String,
    val recentFiveDiaries: List<RecentFiveDiary>
)

class RecentFiveDiary(
    val content: String,
    val diaryEntryDate: String,
    val emotion: String
)
