package com.nabi.domain.model.home

data class HomeInfo(
    val consecutiveWritingDays: Int,
    val nickname: String,
    val recentFiveDiaries: List<RecentFiveDiary>
)

class RecentFiveDiary(
    val diaryId: Int,
    val content: String,
    val diaryEntryDate: String,
    val emotion: String,
    val isBookmarked: Boolean
)
